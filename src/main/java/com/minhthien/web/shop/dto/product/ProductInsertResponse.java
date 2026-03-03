package com.minhthien.web.shop.dto.product;

import com.minhthien.web.shop.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class ProductInsertResponse {
    private Long productId;
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private Long categoryId;
    private Integer stock;
    private ProductStatus status;

    private String imageUrl;
}
