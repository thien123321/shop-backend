package com.minhthien.web.shop.dto.pay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayOSWebhookRequest {

    private String code;
    private String desc;
    private Boolean success;
    private WebhookData data;
    private String signature;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WebhookData {

        private Long orderCode;
        private Long amount;
        private String description;
        private String reference;
        private String transactionDateTime;
        private String paymentLinkId;
        private String currency;
        private String counterAccountName;
    }
}
