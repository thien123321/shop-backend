package com.minhthien.web.shop.dto.pay;

import lombok.Data;

@Data
public class PayOSCreatePaymentRequest {
    private Long orderCode;
    private Integer amount;
    private String description;
    private String returnUrl;
    private String cancelUrl;
}
