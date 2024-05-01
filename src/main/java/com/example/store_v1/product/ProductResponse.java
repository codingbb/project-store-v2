package com.example.store_v1.product;

import lombok.Builder;
import lombok.Data;

public class ProductResponse {

    // 1. 상품 상세보기 화면을 위한 DTO
    @Data
    public static class DetailDTO {
        private Integer id;
        private String name;
        private Integer price;
        private Integer qty;
        private String imgFileName;

        @Builder
        public DetailDTO(Product product) {
            this.id = product.getId();
            this.name = product.getName();
            this.price = product.getPrice();
            this.qty = product.getQty();
            this.imgFileName = product.getImgFileName();
        }
    }

    // 2. 상품 수정하기 화면을 위한 DTO
    @Data
    public static class UpdateDTO {
        private Integer id;
        private String name;
        private Integer price;
        private Integer qty;
        private String imgFileName;

        @Builder
        public UpdateDTO(Product product) {
            this.id = product.getId();
            this.name = product.getName();
            this.price = product.getPrice();
            this.qty = product.getQty();
            this.imgFileName = product.getImgFileName();
        }
    }

    // 3. 상품 목록보기 화면을 위한 DTO
    @Data
    public static class MainDTO {
        private Integer id;
        private String name;
        private Integer price;
        private Integer qty;
        private String imgFileName;

        @Builder
        public MainDTO(Product product) {
            this.id = product.getId();
            this.name = product.getName();
            this.price = product.getPrice();
            this.qty = product.getQty();
            this.imgFileName = product.getImgFileName();
        }

    }

    //상품 리스트 dto
    @Data
    public static class ListDTO {
        private Integer id;
        private String name;
        private Integer price;
        private Integer qty;
        private Integer indexNumb;

        @Builder
        public ListDTO(Product product) {
            this.id = product.getId();
            this.name = product.getName();
            this.price = product.getPrice();
            this.qty = product.getQty();
            this.indexNumb = product.getIndexNumb();
        }

    }


}
