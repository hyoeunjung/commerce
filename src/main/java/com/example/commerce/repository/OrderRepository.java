package com.example.commerce.repository;


import com.example.commerce.entity.Order;
import com.example.commerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUser(User user);

    Optional<Order> findByIdAndUser(Long orderId, User user);
}
