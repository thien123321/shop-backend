package com.minhthien.web.shop.dto.product;

import lombok.Data;

@Data
public class ProductReviewRequest {
    private Long productId;
    private Integer rating;
    private String comment;
}
