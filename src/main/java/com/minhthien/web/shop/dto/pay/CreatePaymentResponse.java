package com.minhthien.web.shop.dto.pay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentResponse {
    private Long orderId;
    private Long paymentId;
    private String status;
    private String paymentUrl;
}
