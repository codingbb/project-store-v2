package com.example.store_v1.product;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

public class ProductRequest {

    @Data
    public static class UpdateDTO {
        private String name;
        private Integer price;
        private Integer qty;
    }

    @Data
    public static class SaveDTO {
        private String name;
        private Integer price;
        private Integer qty;

    }

}
