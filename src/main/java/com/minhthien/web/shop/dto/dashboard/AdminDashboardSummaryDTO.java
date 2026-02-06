package com.minhthien.web.shop.dto.dashboard;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AdminDashboardSummaryDTO {

    private Long totalUsers;
    private Long totalProducts;
    private Long totalOrders;
    private BigDecimal totalRevenue;
}
