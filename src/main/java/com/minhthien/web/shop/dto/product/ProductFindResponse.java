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
public class ProductFindResponse {

    private Long productId;
    private String productName;
    private BigDecimal price;
    private String description;
    private String imageUrl;
    private ProductStatus status;
}
