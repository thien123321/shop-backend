package com.minhthien.web.shop.dto.dashboard;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class StaffDashboardSummaryDTO {

    private Long totalOrders;
    private Long ordersToday;
    private BigDecimal revenueToday;
    private Long lowStockProducts;
}
