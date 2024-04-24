package com.example.store_v1.product;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepo;

    //상품 삭제하기
    @Transactional
    public void deleteById(Integer id) {
        //추후 delete 할 때, 존재하는지 확인하고 들어갈 것. 지금은 생략
        productRepo.deleteById(id);
    }


    // 상품 업데이트
    @Transactional
    public void updateById(Integer id, ProductRequest.UpdateDTO requestDTO) {
        productRepo.updateById(id, requestDTO);

    }

    //상품 업데이트 폼 보기
    public Product findByIdUpdate(Integer id) {
        Product product = productRepo.findById(id);
        return product;
    }


    //상품 리스트 목록 보기
    public List<Product> findAllList() {
        List<Product> productList = productRepo.findAll();

        //pk로 no 주니까 너무 지저분해져서 no용 필드를 새로 만들어줌
        Integer indexNumb = productList.size();
        for (Product product : productList) {
            product.setIndexNumb(indexNumb--);
        }

        //엔티티 받아온걸 dto로 변경
        return productList;
    }


    //상품 상세보기
    public Product findByIdDetail(Integer id) {
        Product product = productRepo.findById(id);
        return product;
    }

    //상품 등록
    @Transactional
    public void save(ProductRequest.SaveDTO requestDTO) {
        productRepo.save(requestDTO);
    }


    //상품 메인 목록 보기
    public List<Product> findAllMain() {
        List<Product> productList = productRepo.findAll();

        //엔티티 받아온걸 dto로 변경
        return productList;
    }

}
