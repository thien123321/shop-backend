package com.minhthien.web.shop.service.dashboard;

import com.minhthien.web.shop.dto.dashboard.AdminDashboardSummaryDTO;
import com.minhthien.web.shop.dto.dashboard.RevenueChartDTO;
import com.minhthien.web.shop.dto.dashboard.StaffDashboardSummaryDTO;
import com.minhthien.web.shop.entity.product.Product;
import com.minhthien.web.shop.enums.RevenueRange;
import com.minhthien.web.shop.repository.auth.UserRepository;
import com.minhthien.web.shop.repository.pay.OrderRepository;
import com.minhthien.web.shop.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    // ===== ADMIN =====
    public AdminDashboardSummaryDTO getAdminSummary() {
        return AdminDashboardSummaryDTO.builder()
                .totalUsers(userRepo.countUsers())
                .totalProducts(productRepo.countProducts())
                .totalOrders(orderRepo.countTotalOrders())
                .totalRevenue(orderRepo.getTotalRevenue())
                .build();
    }

    // ===== STAFF =====
    public StaffDashboardSummaryDTO getStaffSummary() {

        // 🔥 FIX TODAY RANGE
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        return StaffDashboardSummaryDTO.builder()
                .totalOrders(orderRepo.countTotalOrders())
                .ordersToday(orderRepo.countOrdersToday(start, end))
                .revenueToday(orderRepo.revenueToday(start, end))
                .lowStockProducts((long) productRepo.lowStockProducts().size())
                .build();
    }

    // ===== LOW STOCK =====
    public List<Product> getLowStock() {
        return productRepo.lowStockProducts();
    }

    // ===== REVENUE CHART =====
    public List<RevenueChartDTO> getRevenueChart(RevenueRange range) {

        LocalDateTime now = LocalDateTime.now();

        return switch (range) {

            case H1 -> mapHour(orderRepo.revenueByHour(now.minusHours(1)));
            case H15 -> mapHour(orderRepo.revenueByHour(now.minusHours(15)));
            case H24 -> mapHour(orderRepo.revenueByHour(now.minusHours(24)));

            case D3 -> mapDay(orderRepo.revenueByDay(now.minusDays(3)));
            case D15 -> mapDay(orderRepo.revenueByDay(now.minusDays(15)));
            case D30 -> mapDay(orderRepo.revenueByDay(now.minusDays(30)));
            case D45 -> mapDay(orderRepo.revenueByDay(now.minusDays(45)));

            case Y1 -> mapMonth(orderRepo.revenueByMonth(now.minusYears(1)));
            case Y2 -> mapMonth(orderRepo.revenueByMonth(now.minusYears(2)));
        };
    }

    // ===== MAPPER =====

    private List<RevenueChartDTO> mapHour(List<Object[]> data) {
        return data.stream()
                .map(o -> RevenueChartDTO.builder()
                        .label(o[0] + "h")
                        .revenue((BigDecimal) o[1])
                        .build())
                .toList();
    }

    private List<RevenueChartDTO> mapDay(List<Object[]> data) {
        return data.stream()
                .map(o -> RevenueChartDTO.builder()
                        .label(o[0].toString())
                        .revenue((BigDecimal) o[1])
                        .build())
                .toList();
    }

    private List<RevenueChartDTO> mapMonth(List<Object[]> data) {
        return data.stream()
                .map(o -> RevenueChartDTO.builder()
                        .label(o[1] + "/" + o[0])
                        .revenue((BigDecimal) o[2])
                        .build())
                .toList();
    }
}