package com.minhthien.web.shop.entity.product;

import com.minhthien.web.shop.entity.Auth.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_reports")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String reason;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String status; // PENDING

    private LocalDateTime createdAt;
}
