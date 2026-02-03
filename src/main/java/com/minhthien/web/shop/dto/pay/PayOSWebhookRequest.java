package com.minhthien.web.shop.dto.pay;

import lombok.Data;

@Data
public class PayOSWebhookRequest {

    private String event;
    private Data data;
    private String signature;

    @lombok.Data
    public static class Data {
        private Long orderCode;
        private Integer amount;
        private String status;
    }
}
