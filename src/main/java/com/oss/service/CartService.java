package com.oss.service;

import com.oss.model.Cart;
import com.oss.model.CartItem;
import com.oss.model.Product;
import com.oss.model.User;
import com.oss.repository.CartRepository;
import com.oss.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    public void addToCart(HttpSession session, String productId, User user) {
        if (user == null) {
            addToTemporaryCart(session, productId);
        } else {
            addToUserCart(user, productId);
        }
    }

    public void addToTemporaryCart(HttpSession session, String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID is invalid");
        }

        List<CartItem> tempCart = (List<CartItem>) session.getAttribute("cart");
        if (tempCart == null) {
            tempCart = new ArrayList<>();
            session.setAttribute("cart", tempCart);
        }

        boolean productExists = false;
        for (CartItem item : tempCart) {
            if (item.getProduct().getProductId().equals(productId)) {
                item.setQuantity(item.getQuantity() + 1); // Update quantity
                productExists = true;
                break;
            }
        }

        if (!productExists) {
            Optional<Product> product = productRepository.findById(Long.valueOf(productId));
            if (product.isPresent()) {
                CartItem newItem = new CartItem();
                newItem.setProduct(product.get());
                newItem.setQuantity(1);
                tempCart.add(newItem);
            }
        }
    }

    public void addToUserCart(User user, String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID is invalid");
        }

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return newCart;
        });
        Set<CartItem> cartItems = cart.getCartItems();

        boolean productExists = false;
        for (CartItem item : cartItems) {
            if (item.getProduct().getProductId().equals(productId)) {
                item.setQuantity(item.getQuantity() + 1); // Update quantity
                productExists = true;
                break;
            }
        }

        if (!productExists) {
            Optional<Product> product = productRepository.findById(Long.valueOf(productId));
            if (product.isPresent()) {
                CartItem newItem = new CartItem();
                newItem.setProduct(product.get());
                newItem.setCart(cart);
                newItem.setQuantity(1);
                cartItems.add(newItem);
            }
        }

        cart.setCartItems(cartItems);
        cartRepository.save(cart);
    }

    public List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        return cart;
    }

    public int getCartSize(HttpSession session, User user) {
        if (user == null) {
            return getCart(session).stream().mapToInt(CartItem::getQuantity).sum();
        } else {
            return cartRepository.findByUser(user)
                    .map(cart -> cart.getCartItems().stream().mapToInt(CartItem::getQuantity).sum())
                    .orElse(0);
        }
    }


}
