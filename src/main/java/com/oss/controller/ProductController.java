package com.oss.controller;

import com.oss.model.Category;
import com.oss.model.Product;
import com.oss.model.ProductImage;
import com.oss.service.CategoryService;
import com.oss.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Controller
public class ProductController {


    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String getProduct(Model model) {
        List<Product> productList = productService.getAllProducts();
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("productlist", productList);
        model.addAttribute("categorylist", categoryList);
        return "inventory/productlist";
    }

    @PostMapping("/productdetail")
    public String getProductDetail(@RequestParam("id") Long id, Model model) {
        if(id == null) {
            return getProduct(model);
        }
        Product product = productService.getProductById(id);
        List<ProductImage> productImage = productService.getProductImagesByProductId(product);
        model.addAttribute("productdetail", product);
        model.addAttribute("imagelist", productImage);
        return getProduct(model);
    }

    @PostMapping("/editproduct")
    public String editProductDetail(){
        return "inventory/product";
    };

    @PostMapping("/addproduct")
    public String addProduct(@RequestParam("pname") String name,
                             @RequestParam("pcode") String code,
                             @RequestParam("pprice") Double price,
                             @RequestParam("psale") Double sale,
                             @RequestParam("pcategory") Long categoryId,
                             @RequestParam("pdescription") String description,
                             @RequestParam("pimage") MultipartFile image,
                             Model model){

        Product product = new Product();
        // Set information for product
        product.setProductCode(code);
        product.setProductName(name);
        product.setCategory(categoryService.getCategoryById(categoryId));
        product.setPrice(price);
        product.setSales(sale);
        product.setDescription(description);
        product.setAddedDate(new Date());
        product.setQuantity(0);
        Long productId = productService.saveProduct(product).getProductId();

        // Upload file
        if (image.isEmpty()) {
            model.addAttribute("addmessage", "Image is required");
            return "redirect:/addproduct";
        } else {
            String uploadFolder = "src/main/resources/static/assets/images/product/";
            try {
                // Generate a unique filename for the image
                String filename = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                Path path = Paths.get(uploadFolder + filename);

                // Ensure the directory exists
                Files.createDirectories(path.getParent());

                // Save image file to the system
                byte[] bytes = image.getBytes();
                Files.write(path, bytes);

                // Set image path for product
                ProductImage productImage = new ProductImage();
                productImage.setProduct(product);
                productImage.setImageUrl(filename);
                productService.saveProductImage(productImage);

                model.addAttribute("addmessage", "Product added successfully");
                return "redirect:/products";

            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("addmessage", "Failed to upload file");
                return "redirect:/addproduct";
            }
        }
    }






}