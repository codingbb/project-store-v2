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

    // 상품 업데이트
    @Transactional
    public void updateById(Integer id, ProductRequest.UpdateDTO requestDTO, String imgFileName) {
        productRepo.updateById(id, requestDTO, imgFileName);

    }


    //상품 삭제하기
    @Transactional
    public void deleteById(Integer id) {
        //추후 delete 할 때, 존재하는지 확인하고 들어갈 것. 지금은 생략
        productRepo.deleteById(id);
    }


    //상품 상세보기
    public ProductResponse.DetailDTO findByIdDetail(Integer id) {
        Product product = productRepo.findById(id);
        return new ProductResponse.DetailDTO(product);
    }

    //상품 업데이트 폼 보기
    public ProductResponse.UpdateDTO findByIdUpdate(Integer id) {
        Product product = productRepo.findById(id);
        return new ProductResponse.UpdateDTO(product);
    }


    //상품 메인 목록 보기
    public List<ProductResponse.MainDTO> findAllMain() {
        List<Product> productList = productRepo.findAll();

        //엔티티 받아온걸 dto로 변경
        return productList.stream().map(product ->
                new ProductResponse.MainDTO(product)).collect(Collectors.toList());
    }

    //상품 리스트 목록 보기
    public List<ProductResponse.ListDTO> findAllList() {
        List<Product> productList = productRepo.findAll();

        //pk로 no 주니까 너무 지저분해져서 no용 필드를 새로 만들어줌
        Integer indexNumb = productList.size();
        for (Product product : productList) {
            product.setIndexNumb(indexNumb--);
        }

        //엔티티 받아온걸 dto로 변경
        return productList.stream().map(product ->
                new ProductResponse.ListDTO(product)).collect(Collectors.toList());
    }


    //상품 등록
    @Transactional
    public void save(ProductRequest.SaveDTO requestDTO, String imgFileName) {
        productRepo.save(requestDTO, imgFileName);
    }

    //상품명 실시간 중복체크
    public Product findByName(String name) {
        Product product = productRepo.findByName(name);
        return product;
    }

    //상품명 실시간 중복체크(업데이트)
    public Product findByNameUpdate(String name, Integer id) {
        Product product = productRepo.findByNameUpdate(name, id);
        return product;
    }

}
