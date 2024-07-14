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
    List<Product> findTop10Products(Pageable pageable);
    @Query("SELECT p FROM Product p ORDER BY p.sales DESC")
    List<Product> findTop10BestSellingProducts();
}

