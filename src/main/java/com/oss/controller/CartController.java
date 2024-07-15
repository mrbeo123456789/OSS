package com.oss.controller;

import com.oss.model.User;
import com.oss.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
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
    }

