package com.minhthien.web.shop.dto.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductReportResponse {

    private String message;
    private String reason;
    private Long totalReports;
}
