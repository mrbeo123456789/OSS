package com.oss.repository;

import com.oss.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p")
    List<Product> findAllProduct();
    @Query("SELECT p FROM Product p ORDER BY p.addedDate DESC")
    List<Product> findTop10Products();
    @Query("SELECT p FROM Product p ORDER BY p.sales DESC")
    List<Product> findTop10BestSellingProducts();
    @Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId")
    List<Product> findProductsByCategoryId(Long categoryId);
}

