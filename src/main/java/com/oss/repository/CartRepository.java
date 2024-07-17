package com.oss.repository;

import com.oss.model.Cart;
import com.oss.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
    @Query("SELECT c FROM Cart c JOIN FETCH c.cartItems ci JOIN FETCH ci.product p JOIN FETCH p.images WHERE c.user = :user")
    Optional<Cart> findByUserWithProductsAndImages(User user);
}
