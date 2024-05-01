# final project - 상품 판매 사이트 2단계

<hr>

## [ 2단계 기능 구현 ]
* 상품 이미지 insert 및 update
* AJAX를 이용해 상품명 실시간 중복 검사

<hr>

## 1. 상품 이미지 insert 

### 0. 이미지 파일은 바이트 배열로 읽어온다.
```

-> 바이트 배열을 Base64로 인코딩하여 문자열로 변환
-> 변환된 문자열을 HTTP를 통해 전송
-> Base64로 인코딩된 문자열 데이터를 받음
-> 문자열을 Base64 디코딩하여 원래의 바이트 배열로 변환
-> 바이트 배열을 사용하여 이미지 파일을 복원하거나 필요한 작업 수행

그걸 
MultipartFile imgFile = requestDTO.getImgFile();
이 객체가 알아서 해준다!

그리고 reqeuestDTO와 imgFileName을 DB에 저장해줌
* imgFileName은 UUID(롤링)해서 'DB'에 이미지 '주소'를 저장한 다음,
  불러올 때 이 이미지 '주소'를 불러오는 것. 이미지 자체는 서버에 저장 됨.

```

### 0. WebMvcConfig
```java
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

        registry
                .addResourceHandler("/upload/**")
                .addResourceLocations("file:./upload/")
                .setCachePeriod(60 * 60)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());

    }
}
```

### 1-1. product_tb에 imgFileName 필드 생성
![image](https://github.com/codingbb/project-store-v2/assets/153585866/edae554f-0abc-4227-a40a-33837b1af0f0)

### 1-2. requestDTO 생성
```java
    @Data
    public static class SaveDTO {
        private String name;
        private Integer price;
        private Integer qty;
        private MultipartFile imgFile;

    }
```
* MultipartFile 로 받아주었다.


### 1-3. ProductController
```java
@PostMapping("/product/save")
    public String save(ProductRequest.SaveDTO requestDTO) {
        System.out.println(requestDTO);

        MultipartFile imgFile = requestDTO.getImgFile();
        String imgFileName = UUID.randomUUID() + "_" + imgFile.getOriginalFilename();

//        Path imgPath = Paths.get("./src/main/resources/static/upload/" + imgFileName);
        Path imgPath = Paths.get("./upload/" + imgFileName);

        try {
            //upload 디렉토리가 존재하지 않는다면, 서버가 시작될 때 해당 디렉토리를 자동으로 생성
            Files.createDirectories(imgPath.getParent());
            Files.write(imgPath, imgFile.getBytes());
            productService.save(requestDTO, imgFileName);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/product";
    }
```

### 1-4. ProductService
```java
    @Transactional
    public void save(ProductRequest.SaveDTO requestDTO, String imgFileName) {
        productRepo.save(requestDTO, imgFileName);
    }
```

### 1-5. ProductRepository
```java
    //상품 등록
    public void save(ProductRequest.SaveDTO requestDTO, String imgFileName) {
        String q = """
                insert into product_tb(name, price, qty, img_file_name, created_at) values (?, ?, ?, ?, now())
                """;

        Query query = em.createNativeQuery(q);
        query.setParameter(1, requestDTO.getName());
        query.setParameter(2, requestDTO.getPrice());
        query.setParameter(3, requestDTO.getQty());
        query.setParameter(4, imgFileName);
        query.executeUpdate();
    }
```

<hr>

## 2. 이미지 수정 (Update)
### 2-1. 이미지 업데이트
```java
//업데이트
    @PostMapping("/product/{id}/update")
    public String update(@PathVariable Integer id, ProductRequest.UpdateDTO requestDTO) {

        String imgFileName;

        // 이미지 파일이 존재할 경우, 새 파일명 생성 및 파일 저장
        if (!requestDTO.getImgFile().isEmpty()) {
            MultipartFile imgFile = requestDTO.getImgFile();
            imgFileName = UUID.randomUUID() + "_" + imgFile.getOriginalFilename();

            Path imgPath = Paths.get("./upload/" + imgFileName);

            try {
                //upload 디렉토리가 존재하지 않는다면, 서버가 시작될 때 해당 디렉토리를 자동으로 생성하는 코드
                //static에 안 넣으려고 설정해줬나봄
                Files.createDirectories(imgPath.getParent());
                Files.write(imgPath, imgFile.getBytes());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else {
            // 이미지 파일이 없을 경우, 기존 파일명 유지
            ProductResponse.UpdateDTO existImg = productService.findByIdUpdate(id);
            imgFileName = existImg.getImgFileName(); // 기존의 imgFileName을 가져와서 사용

        }

        productService.updateById(id, requestDTO, imgFileName);

        return "redirect:/product/" + id;
    }
```

```
requestDTO.getImgFile이 비어있지 않으면, 정상적으로 UUID를 사용해 새로운 이미지를 저장하고,
새롭게 받은 이미지 파일이 없으면 기존의 업데이트 폼에 있던 정보를 조회해서
그 정보를 imgFileName 변수에 할당, 
productService.updateById(id, requestDTO, imgFileName); 이렇게 업데이트 한다!
```

<hr>

## 3. 이미지 경로
![image](https://github.com/codingbb/project-store-v2/assets/153585866/acea27b1-7fab-463b-bc7c-1d7f0d025c48)

<hr>

## 4. AJAX를 이용한 상품명 실시간 중복 체크
* JavaScript 사용

### 0. ApiUtil
```java
@Data
public class ApiUtil<T> {
    private Integer status;
    private String msg;
    private T body;

    public ApiUtil(T body) {
        this.status = 200;
        this.msg = "성공";
        this.body = body;
    }

    public ApiUtil(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
        this.body = null;
    }
}
```
  
### 4-1. 상품 insert 실시간 중복 체크
```javascript
// 실시간 상품명 중복체크 (save용)
$("#name").keyup(function (){
    //this = 지금 현재 클릭한 것, val = 값 가져옴
    var name = $(this).val();
    // alert(name);

    //서버로 가서 id 중복체크 -> url과 입력 데이터는 바뀌면 안됨 -> Ajax
    //url -> /product/name-check
    //서버에서 전달되는 데이터를 result로 받음 -> 가져온 데이터가 null이면 사용 가능, 있으면 중복
    var encodedName = encodeURIComponent(name); //이게 없으면 띄어쓰기 인식 안됨

    $.ajax({
        method: "GET",
        url: "/product/name-check?name="+encodedName
    }).done((res)=>{

        console.log(res);
        if (res.body === true) {
            $("#nameCheck").removeClass("alert-danger");
            $("#nameCheck").addClass("alert-success");
            $("#nameCheck").text("사용 가능한 상품명 입니다.");
        } else {
            $("#nameCheck").removeClass("alert-success");
            $("#nameCheck").addClass("alert-danger");
            $("#nameCheck").text("중복된 상품명 입니다.");
        }
    }).fail((res)=>{
        alert("통신 오류");
    });

});

```
### 4-2. ProductController
```java
    @GetMapping("/product/name-check")
    public @ResponseBody ResponseEntity<?> nameSameCheck(String name) {
        Product product = productService.findByName(name);
        if (product == null) {
            return ResponseEntity.ok(new ApiUtil<>(true)); //상품 등록 가능
        } else {
            //response.setStatus(400);
            return ResponseEntity.ok(new ApiUtil<>(false)); //상품 등록 불가
        }
    }
```

### 4-3. ProductService
```java
    //상품명 실시간 중복체크
    public Product findByName(String name) {
        Product product = productRepo.findByName(name);
        return product;
    }
```

### 4-4. ProductRepository
```java
    public Product findByName(String name) {
        try {
            String q = """
                    select * from product_tb where name = ?
                    """;
            Query query = em.createNativeQuery(q, Product.class);
            query.setParameter(1, name);
            Product product = (Product) query.getSingleResult();
            return product;

        } catch (NoResultException e) {
            return null;
        }
    }
```

<hr>

## 5. 상품 update 실시간 중복 체크
* 자기자신의 상품명을 빼고 나머지랑 비교해야 함 

### 5-1. ProductRepository
```java
    public Product findByNameUpdate(String name, Integer id) {
        try {
            String q = """
                    select * from product_tb where name = ? and id != ?
                    """;
            Query query = em.createNativeQuery(q, Product.class);
            query.setParameter(1, name);
            query.setParameter(2, id);
            Product product = (Product) query.getSingleResult();
            return product;

        } catch (NoResultException e) {
            return null;
        }
    }

```

