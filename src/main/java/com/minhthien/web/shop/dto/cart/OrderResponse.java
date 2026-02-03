package com.minhthien.web.shop.dto.cart;

import com.minhthien.web.shop.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private Long orderCode;
    private BigDecimal amount;
    private OrderStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
}
