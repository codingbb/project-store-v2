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
