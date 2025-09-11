package com.example.commerce.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private boolean isDeleted = false;

    public void update(String name, Integer price, Integer stock, String description) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    public void softDelete() {
        this.isDeleted = true;
    }



}
