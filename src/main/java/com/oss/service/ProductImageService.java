package com.oss.service;

import com.oss.model.ProductImage;
import com.oss.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductImageService {
    @Autowired
    ProductImageRepository productImageRepository;


}
