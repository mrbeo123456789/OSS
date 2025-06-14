package com.oss.controller;

import com.oss.model.Category;
import com.oss.model.Product;
import com.oss.service.CategoryService;
import com.oss.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;

    @GetMapping("/")
    public String home(Model model) {
        List<Product> getAllProducts = productService.getAllProducts();
        List<Category> getAllCategories = categoryService.getAllCategories();
        //lấy 10 sản phẩm mới nhất theo thời gian được quản lý kho thêm vào
        List<Product> getNewProducts = productService.getTop10NewestProducts();
        //lấy 10 sản phẩm được giảm giá cao nhất theo %
        List<Product> getBestSellerProducts = productService.getBestSellerProducts();
        model.addAttribute("products", getAllProducts);
        model.addAttribute("categories", getAllCategories);
        model.addAttribute("newProducts", getNewProducts);
        model.addAttribute("bestSellerProducts", getBestSellerProducts);
        return "common/home";
    }


}
