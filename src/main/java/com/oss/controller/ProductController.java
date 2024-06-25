package com.oss.controller;

import com.oss.model.Product;
import com.oss.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProductController {


    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String getProduct(Model model) {
        List<Product> productList = productService.getAllProducts();
        model.addAttribute("productlist", productList);
        return "inventory/productlist";
    }


}