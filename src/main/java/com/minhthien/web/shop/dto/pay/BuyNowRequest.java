package com.minhthien.web.shop.dto.pay;

import lombok.Data;

@Data
public class BuyNowRequest {
    private Long productId;
    private Integer quantity;
}
