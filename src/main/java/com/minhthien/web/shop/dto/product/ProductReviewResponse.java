package com.minhthien.web.shop.dto.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductReviewResponse {

    private String message;
    private Integer rating;
    private Double avgRating;
    private Integer totalReviews;
}
