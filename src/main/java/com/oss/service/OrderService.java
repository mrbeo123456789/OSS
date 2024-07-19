package com.oss.service;

import com.oss.model.Order;
import com.oss.model.OrderItem;
import com.oss.repository.OrderItemRepository;
import com.oss.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Transactional
    public OrderItem saveOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }
    public  Order getOrderById(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        return optionalOrder.orElse(null); // Hoặc throw exception nếu không tìm thấy
    }

    public List<Order> findOrdersByUserId(Long userId){
        return orderRepository.findOrdersByUserId(userId);
    }
}
