package com.oss.controller;

import com.google.gson.JsonObject;
import com.oss.model.CartItem;
import com.oss.model.Product;
import com.oss.model.ProductImage;
import com.oss.model.User;
import com.oss.service.CartService;
import com.oss.service.GhnService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {


    @Autowired
    private CartService cartService;

        @PostMapping("/add-to-cart")
        public ResponseEntity<Map<String, Object>> addToCart(@RequestBody Map<String, Object> requestData, HttpSession session) {
            String productId = (String) requestData.get("productId");

            if (productId == null || productId.trim().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("cartSize", 0);
                return ResponseEntity.ok(response);
            }

            User user = (User) session.getAttribute("user");
            cartService.addToCart(session, productId, user);

            int cartSize = cartService.getCartSize(session, user);

            Map<String, Object> response = new HashMap<>();
            response.put("cartSize", cartSize);

            return ResponseEntity.ok(response);
        }

    @GetMapping("/viewcart")
    public String viewCart(Model model,HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<CartItem> cartItems;

        if (user == null) {
            // Get cart items from the session for not logged-in users
            cartItems = cartService.getCart(session);

        } else {
            // Get cart items from the database for logged-in users
            cartItems = cartService.getCartByUser(user);
        }
        double totalPrice = cartItems.stream()
                .mapToDouble(cartItem -> cartItem.getProduct().getPrice() * cartItem.getQuantity())
                .sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        // Add cart items to the model



        // Return the view name to be rendered
       return "customer/cart";
    }
    @PostMapping("/update-cart")
    public ResponseEntity<Map<String, Object>> updateCart(@RequestBody Map<String, Object> requestData, HttpSession session) {
        String productId = (String) requestData.get("productId");
        String tmp = (String) requestData.get("quantity");
        int quantity = Integer.parseInt(tmp);

        User user = (User) session.getAttribute("user");
        cartService.updateCart(session, productId, quantity, user);

        List<CartItem> cartItems;
        if (user == null) {
            cartItems = cartService.getCart(session);
        } else {
            cartItems = cartService.getCartByUser(user);
        }

        double totalPrice = cartItems.stream()
                .mapToDouble(cartItem -> cartItem.getProduct().getPrice() * cartItem.getQuantity())
                .sum();

        Map<String, Object> response = new HashMap<>();
        response.put("totalPrice", totalPrice);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/remove-from-cart")
    public ResponseEntity<Map<String, Object>> removeFromCart(@RequestBody Map<String, Object> requestData, HttpSession session) {
        String productId = (String) requestData.get("productId");

        User user = (User) session.getAttribute("user");
        cartService.removeFromCart(session, productId, user);

        List<CartItem> cartItems;
        if (user == null) {
            cartItems = cartService.getCart(session);
        } else {
            cartItems = cartService.getCartByUser(user);
        }

        double totalPrice = cartItems.stream()
                .mapToDouble(cartItem -> cartItem.getProduct().getPrice() * cartItem.getQuantity())
                .sum();

        Map<String, Object> response = new HashMap<>();
        response.put("totalPrice", totalPrice);

        return ResponseEntity.ok(response);
    }

    }

