package com.minhthien.web.shop.dto.product;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AdminProductReportResponse {
    private Long id;
    private Long productId;
    private String productName;
    private Long userId;
    private String reason;
    private String description;
    private String status;
    private LocalDateTime createdAt;
}
