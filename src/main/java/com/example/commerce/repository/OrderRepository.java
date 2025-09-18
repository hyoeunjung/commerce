package com.example.commerce.repository;


import com.example.commerce.entity.Order;
import com.example.commerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUser(User user);

    Optional<Order> findByIdAndUser(Long orderId, User user);

    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.user = :user")
    List<Order> findByUserWithOrderItems(@Param("user") User user);

    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.id = :orderId AND o.user = :user")
    Optional<Order> findByIdAndUserWithOrderItems(@Param("orderId") Long orderId, @Param("user") User user);
}
