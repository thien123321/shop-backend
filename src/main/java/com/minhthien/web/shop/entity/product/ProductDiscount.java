package com.minhthien.web.shop.entity.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_discounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    // Giảm theo %
    private Double discountPercent;

    // Giảm theo tiền
    private BigDecimal discountAmount;

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    private Boolean active;
}
