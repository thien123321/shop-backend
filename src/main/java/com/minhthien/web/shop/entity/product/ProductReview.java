package com.minhthien.web.shop.entity.product;

import com.minhthien.web.shop.entity.Auth.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "product_reviews",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"product_id","user_id"}
        )
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Integer rating; // 1 → 5

    @Column(columnDefinition = "TEXT")
    private String comment;

    private LocalDateTime createdAt;
}
