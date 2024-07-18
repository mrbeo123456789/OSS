package com.oss.repository;

import com.oss.model.Cart;
import com.oss.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItem, Long> {
}
