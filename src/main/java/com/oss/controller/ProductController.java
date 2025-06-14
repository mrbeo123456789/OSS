package com.oss.controller;

import com.oss.model.Category;
import com.oss.model.Product;
import com.oss.model.ProductImage;
import com.oss.service.CategoryService;
import com.oss.service.ProductImageService;
import com.oss.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Controller
public class ProductController {


    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductImageService productImageService;

    @GetMapping("/products")
    public String getProducts(Model model) {
        List<Product> productList = productService.getAllProducts();
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("productlist", productList);
        model.addAttribute("categorylist", categoryList);
        return "inventory/product";
    }

    @PostMapping("/products")
    public String getProduct(Model model, @RequestParam("id") Long id) {
        if (id != null) {
            Product product = productService.getProductById(id);
            List<ProductImage> productImage = productService.getProductImagesByProductId(product);
            model.addAttribute("productdetail", product);
            model.addAttribute("imagelist", productImage);
        }
        List<Product> productList = productService.getAllProducts();
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("productlist", productList);
        model.addAttribute("categorylist", categoryList);
        return "inventory/product";
    }

//    @PostMapping("/productdetail")
//    public String getProductDetail(@RequestParam("id") Long id, Model model) {
//        if(id == null) {
//            return getProduct(model);
//        }
//        Product product = productService.getProductById(id);
//        List<ProductImage> productImage = productService.getProductImagesByProductId(product);
//        model.addAttribute("productdetail", product);
//        model.addAttribute("imagelist", productImage);
//        return getProduct(model);
//    }


    @PostMapping("/addproduct")
    public String addProduct(@RequestParam("pname") String name,
                             @RequestParam("pcode") String code,
                             @RequestParam("pprice") Double price,
                             @RequestParam("psale") Double sale,
                             @RequestParam("pcategory") Long categoryId,
                             @RequestParam("pdescription") String description,
                             @RequestParam("pimage") MultipartFile image,
                             Model model) {

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
            String uploadFolder = "./image/product/"; // adjust this path
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
                productImage.setImageUrl("/image/product/" + filename); // No leading ./
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


    @GetMapping("/shop")
    public String showShopPage(Model model) {
        List<Product> getAllProducts = productService.getAllProducts();
        List<Category> getAllCategories = categoryService.getAllCategories();
        model.addAttribute("products", getAllProducts);
        model.addAttribute("categories", getAllCategories);
        return "customer/shop";
    }


    @GetMapping("/viewproduct/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        //lấy thông tin sản phẩm
        Product product = productService.getProductById(id);
        //sản phẩm liên quan, lấy cùng một category
        List<Product> relatedProducts = productService.getRelatedProducts(id);
        if (product == null) {
            // Handle product not found, maybe redirect to an error page
            return "redirect:/error";
        }
        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", relatedProducts);
        return "/customer/productview"; // Return the view name
    }

    @PostMapping("/editproduct")
    public String editProduct(@RequestParam("productId") Long productId,
                              @RequestParam("pname") String name,
                              @RequestParam("pcode") String code,
                              @RequestParam("pprice") Double price,
                              @RequestParam("psale") Double sale,
                              @RequestParam("pquantity") int quantity,
                              @RequestParam("pcategory") Long categoryId,
                              @RequestParam("pdescription") String description,
                              Model model) {

        Product product = productService.getProductById(productId);
        if (product != null) {
            // Update product information
            product.setProductName(name);
            product.setProductCode(code);
            product.setPrice(price);
            product.setSales(sale);
            product.setQuantity(quantity);
            product.setCategory(categoryService.getCategoryById(categoryId));
            product.setDescription(description);

            productService.saveProduct(product);
            List<ProductImage> productImage = productService.getProductImagesByProductId(product);
            model.addAttribute("productdetail", product);
            model.addAttribute("imagelist", productImage);

            model.addAttribute("editmessage", "Product updated successfully");
        } else {
            model.addAttribute("editmessage", "Product not found");
        }

//        if (productId != null) {
//            Product productDetail = productService.getProductById(productId);
//            List<ProductImage> productImage = productService.getProductImagesByProductId(product);
//            model.addAttribute("productdetail", productDetail);
//            model.addAttribute("imagelist", productImage);
//        }

        return getProducts(model);
    }

    @PostMapping("/addcategory")
    public String addCategory(@RequestParam("categoryname") String categoryName) {
        if (categoryName != null) {
            Category category = new Category();
            category.setCategoryName(categoryName);
            categoryService.saveCategory(category);
        }
        return "redirect:/products";
    }

    @PostMapping("addproductimage")
    public String addProductImage(@RequestParam("productId") Long id,
                                  @RequestParam("pimage") MultipartFile image,
                                  Model model) {
        if (id != null && image != null) {
            // Upload file
            String uploadFolder = "image/product/";
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
                productImage.setProduct(productService.getProductById(id));
                productImage.setImageUrl("/image/product/" + filename);
                productService.saveProductImage(productImage);

                model.addAttribute("addmessage", "Product added successfully");
                return "redirect:/products";
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("addmessage", "Failed to upload file");
                return "redirect:/addproduct";
            }
        }
        return "redirect:/products";
    }

}