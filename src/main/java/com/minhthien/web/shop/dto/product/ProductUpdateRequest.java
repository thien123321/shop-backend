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
public class ProductUpdateRequest {
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private Integer stock;

    private String imageUrl;
}
