package com.minhthien.web.shop.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class ProductInsertRequest {

    private String productName;
    private String description;
    private BigDecimal price;
    private Long categoryId;
    private Integer stock;

    private String image;
}
