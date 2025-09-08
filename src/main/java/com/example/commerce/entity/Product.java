package com.example.commerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock;

    @Lob
    private String description;

    @Column(nullable = false)
    private boolean isDeleted = false;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "product")
    private List<CartItem> cartItems;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}