package com.example.store_v1.product;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@Controller
public class ProductController {
    private final ProductService productService;

    //상품 삭제
    @PostMapping("/product/{id}/delete")
    public String deleteById(@PathVariable Integer id) {

        return "redirect:/product";
    }

    //상품 목록보기
    @GetMapping("/product")
    public String listForm() {

        return "/product/list";
    }

    //상품 상세보기
    @GetMapping("/product/{id}")
    public String detail(@PathVariable Integer id, HttpServletRequest request) {
        Product product = productService.findByIdDetail(id);
        request.setAttribute("product", product);

        System.out.println(product);
        return "/product/detail";
    }

    // 상품 등록
    @PostMapping("/product/save")
    public String save(ProductRequest.SaveDTO requestDTO) {
        productService.save(requestDTO);

        return "redirect:/product";
    }


    @GetMapping("/product/save-form")
    public String saveForm() {

        return "/product/save-form";
    }

    //업데이트 폼
    @GetMapping("/product/{id}/update-form")
    public String updateForm(@PathVariable Integer id) {


        return "/product/update-form";
    }

    //업데이트
    @PostMapping("/product/{id}/update")
    public String update(@PathVariable Integer id) {


        return "redirect:/product/" + id;
    }


    @GetMapping("/")
    public String main(HttpServletRequest request) {
        List<Product> productList = productService.findAllMain();
        System.out.println(productList);
        request.setAttribute("productList", productList);
        return "/index";
    }


}
