package com.oss.service;

import com.oss.model.Category;
import com.oss.model.Product;
import com.oss.model.ProductImage;
import com.oss.repository.CategoryRepository;
import com.oss.repository.ProductImageRepository;
import com.oss.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    //Product
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {return productRepository.findAllProduct();}


    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product saveProduct(Product product){
        return productRepository.save(product);
    }

    //Product Image
    @Autowired
    private ProductImageRepository productImageRepository;

    public ProductImage saveProductImage(ProductImage productImage){
        return productImageRepository.save(productImage);
    }

    public List<ProductImage> getProductImagesByProductId(Product product){
        return productImageRepository.findByProduct(product);
    }
    public List<Product> getTop10NewestProducts() {
        return productRepository.findTop10Products();
    }

    public List<Product> getBestSellerProducts() {
        return productRepository.findTop10BestSellingProducts();
    }

    public List<Product> getRelatedProducts(Long productId) {
        Long categoryId = getProductById(productId).getCategory().getCategoryId();
        return productRepository.findProductsByCategoryId(categoryId);
    }
}