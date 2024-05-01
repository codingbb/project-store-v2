package com.example.store_v1.product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
    private final EntityManager em;

    //상품 수정하기
    public void updateById(Integer id, ProductRequest.UpdateDTO requestDTO, String imgFileName) {
        String q = """
                update product_tb set name = ?, price = ?, qty = ?, img_file_name = ? where id = ?
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, requestDTO.getName());
        query.setParameter(2, requestDTO.getPrice());
        query.setParameter(3, requestDTO.getQty());
        query.setParameter(4, imgFileName);
        query.setParameter(5, id);
        query.executeUpdate();

    }

    //상품 삭제하기
    public void deleteById(Integer id) {
        String q = """
                delete from product_tb where id = ?
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, id);
        query.executeUpdate();

    }

    //상품 상세보기
    public Product findById(Integer id) {
        String q = """
                select * from product_tb where id = ?
                """;
        Query query = em.createNativeQuery(q, Product.class);
        query.setParameter(1, id);
        Product result = (Product) query.getSingleResult();
        return result;
    }


    //상품 목록보기
    public List<Product> findAll() {
        String q = """
                select * from product_tb order by id desc 
                """;
        Query query = em.createNativeQuery(q, Product.class);
        List<Product> productList = query.getResultList();
        return productList;
    }

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


    //상품명 실시간 중복체크
    //TODO: 레파지토리에서 try-catch를 안하고 싶은데... 어떻게 해야하지?
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

    //상품명 실시간 중복체크
    //TODO: 레파지토리에서 try-catch를 안하고 싶은데... 어떻게 해야하지?
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


}
