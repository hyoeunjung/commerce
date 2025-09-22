package com.example.commerce.repository;

import com.example.commerce.entity.Order;
import com.example.commerce.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findByIdAndUser(Long orderId, User user);

    Page<Order> findByUser(User user, Pageable pageable);


    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.id = :orderId AND o.user = :user")
    Optional<Order> findByIdAndUserWithOrderItems(@Param("orderId") Long orderId, @Param("user") User user);
}