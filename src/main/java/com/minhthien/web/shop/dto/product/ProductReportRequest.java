package com.minhthien.web.shop.dto.product;

import lombok.Data;

@Data
public class ProductReportRequest {
    private Long productId;
    private String reason;
    private String description;
}
