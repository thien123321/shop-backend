package com.minhthien.web.shop.dto.product;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductDiscountResponse {

    private Long id;
    private Long productId;

    private Double discountPercent;
    private BigDecimal discountAmount;

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    private Boolean active;
}
