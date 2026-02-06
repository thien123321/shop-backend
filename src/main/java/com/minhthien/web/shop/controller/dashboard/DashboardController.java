package com.minhthien.web.shop.controller.dashboard;

import com.minhthien.web.shop.dto.dashboard.*;
import com.minhthien.web.shop.entity.product.Product;
import com.minhthien.web.shop.enums.RevenueRange;
import com.minhthien.web.shop.service.dashboard.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService service;

    // ===== ADMIN =====

    @GetMapping("/admin/summary")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public AdminDashboardSummaryDTO adminSummary() {
        return service.getAdminSummary();
    }

    // ===== STAFF =====

    @GetMapping("/staff/summary")
    @PreAuthorize("hasRole('STAFF')")
    public StaffDashboardSummaryDTO staffSummary() {
        return service.getStaffSummary();
    }

    @GetMapping("/staff/low-stock")
    @PreAuthorize("hasRole('STAFF')")
    public List<Product> lowStock() {
        return service.getLowStock();
    }

    @GetMapping("/staff/revenue-chart")
    @PreAuthorize("hasRole('STAFF')")
    public List<RevenueChartDTO> revenueChart(
            @RequestParam RevenueRange range
    ) {
        return service.getRevenueChart(range);
    }
}
