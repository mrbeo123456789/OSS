package com.oss.service;

import com.oss.model.Cart;
import com.oss.model.CartItem;
import com.oss.model.Product;
import com.oss.model.User;
import com.oss.repository.CartItemsRepository;
import com.oss.repository.CartRepository;
import com.oss.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
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

    @Autowired
    private CartItemsRepository cartItemRepository;
    ;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void addToCart(HttpSession session, String productId, User user) {
        if (user == null) {
            addToTemporaryCart(session, productId);
        } else {
            addToUserCart(user, productId);
        }
    }

    @Transactional
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
            if (item.getProduct().getProductId().equals(Long.valueOf(productId))) {
                item.setQuantity(item.getQuantity() + 1); // Update quantity
                productExists = true;
                break;
            }
        }

        if (!productExists) {
            Optional<Product> product = productRepository.findById(Long.valueOf(productId));
            if (product.isPresent()) {
                Product prod = product.get();
                prod.getImages().size(); // Explicitly initialize the collection
                CartItem newItem = new CartItem();
                newItem.setProduct(prod);
                newItem.setQuantity(1);
                tempCart.add(newItem);
            }
        }
    }

    @Transactional
    public void addToUserCart(User user, String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID is invalid");
        }

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return newCart;
        });

        cartRepository.save(cart); // Ensure the cart is saved before adding items

        Set<CartItem> cartItems = cart.getCartItems();
        boolean productExists = false;
        for (CartItem item : cartItems) {
            if (item.getProduct().getProductId().equals(Long.valueOf(productId))) {
                item.setQuantity(item.getQuantity() + 1); // Update quantity
                cartItemRepository.save(item); // Save the updated item
                productExists = true;
                break;
            }
        }

        if (!productExists) {
            Optional<Product> product = productRepository.findById(Long.valueOf(productId));
            if (product.isPresent()) {
                Product prod = product.get();
                prod.getImages().size(); // Explicitly initialize the collection
                CartItem newItem = new CartItem();
                newItem.setProduct(prod);
                newItem.setCart(cart);
                newItem.setQuantity(1);
                cartItems.add(newItem);
                cartItemRepository.save(newItem); // Save the new item
            }
        }

        cart.setCartItems(cartItems);
        cartRepository.save(cart); // Save the cart with updated items
    }

    @Transactional
    public List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        return cart;
    }

    @Transactional
    public List<CartItem> getCartByUser(User user) {
        List<CartItem> cartItems = cartRepository.findByUserWithProductsAndImages(user)
                .map(Cart::getCartItems)
                .map(ArrayList::new)
                .orElseGet(ArrayList::new);

        return cartItems;
    }

    @Transactional
    public int getCartSize(HttpSession session, User user) {
        if (user == null) {
            return getCart(session).stream().mapToInt(CartItem::getQuantity).sum();
        } else {
            return cartRepository.findByUser(user)
                    .map(cart -> cart.getCartItems().stream().mapToInt(CartItem::getQuantity).sum())
                    .orElse(0);
        }
    }

    @Transactional
    public void updateCart(HttpSession session, String productId, int quantity, User user) {
        if (user == null) {
            updateTemporaryCart(session, productId, quantity);
        } else {
            updateUserCart(user, productId, quantity);
        }
    }

    @Transactional
    public void updateUserCart(User user, String productId, int quantity) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID is invalid");
        }

        Cart cart = cartRepository.findByUser(user).orElse(null);
        if (cart == null) {
            return;
        }

        Set<CartItem> cartItems = cart.getCartItems();
        for (CartItem item : cartItems) {
            if (item.getProduct().getProductId().equals(Long.valueOf(productId))) {
                item.setQuantity(quantity);
                cartItemRepository.save(item); // Save the updated item
                break;
            }
        }

        cart.setCartItems(cartItems);
        cartRepository.save(cart); // Save the cart with updated items
    }

    @Transactional
    public void updateTemporaryCart(HttpSession session, String productId, int quantity) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID is invalid");
        }

        List<CartItem> tempCart = (List<CartItem>) session.getAttribute("cart");
        if (tempCart == null) {
            return;
        }

        for (CartItem item : tempCart) {
            if (item.getProduct().getProductId().equals(Long.valueOf(productId))) {
                item.setQuantity(quantity);
                break;
            }
        }
    }

    @Transactional
    public void removeFromCart(HttpSession session, String productId, User user) {
        if (user == null) {
            removeFromTemporaryCart(session, productId);
        } else {
            removeFromUserCart(user, productId);
        }
    }

    @Transactional
    public void removeFromTemporaryCart(HttpSession session, String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID is invalid");
        }

        List<CartItem> tempCart = (List<CartItem>) session.getAttribute("cart");
        if (tempCart == null) {
            return;
        }

        tempCart.removeIf(item -> item.getProduct().getProductId().equals(Long.valueOf(productId)));
    }

    @Transactional
    public void removeFromUserCart(User user, String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID is invalid");
        }

        Cart cart = cartRepository.findByUser(user).orElse(null);
        if (cart == null) {
            return;
        }

        Set<CartItem> cartItems = cart.getCartItems();
        cartItems.removeIf(item -> item.getProduct().getProductId().equals(Long.valueOf(productId)));

        cart.setCartItems(cartItems);
        cartRepository.save(cart); // Save the cart with updated items
    }
}
