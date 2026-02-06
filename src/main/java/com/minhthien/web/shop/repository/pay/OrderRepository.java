package com.minhthien.web.shop.repository.pay;

import com.minhthien.web.shop.entity.pay.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


    Optional<Order> findByOrderCode(Long orderCode);

    // ===== TOTAL REVENUE =====
    @Query("""
        SELECT COALESCE(SUM(o.amount),0)
        FROM Order o
        WHERE o.status = 'PAID'
    """)
    BigDecimal getTotalRevenue();

    // ===== TOTAL ORDERS =====
    @Query("""
        SELECT COUNT(o)
        FROM Order o
    """)
    Long countTotalOrders();

    // ===== ORDERS TODAY =====
    @Query("""
        SELECT COUNT(o)
        FROM Order o
        WHERE DATE(o.createdAt) = CURRENT_DATE
    """)
    Long countOrdersToday();

    // ===== REVENUE TODAY =====
    @Query("""
        SELECT COALESCE(SUM(o.amount),0)
        FROM Order o
        WHERE o.status = 'PAID'
          AND DATE(o.paidAt) = CURRENT_DATE
    """)
    BigDecimal revenueToday();

    // ===== CHART BY HOUR =====
    @Query("""
        SELECT HOUR(o.paidAt), SUM(o.amount)
        FROM Order o
        WHERE o.status = 'PAID'
          AND o.paidAt >= :start
        GROUP BY HOUR(o.paidAt)
        ORDER BY HOUR(o.paidAt)
    """)
    List<Object[]> revenueByHour(LocalDateTime start);

    // ===== CHART BY DAY =====
    @Query("""
        SELECT DATE(o.paidAt), SUM(o.amount)
        FROM Order o
        WHERE o.status = 'PAID'
          AND o.paidAt >= :start
        GROUP BY DATE(o.paidAt)
        ORDER BY DATE(o.paidAt)
    """)
    List<Object[]> revenueByDay(LocalDateTime start);

    // ===== CHART BY MONTH =====
    @Query("""
        SELECT YEAR(o.paidAt), MONTH(o.paidAt), SUM(o.amount)
        FROM Order o
        WHERE o.status = 'PAID'
          AND o.paidAt >= :start
        GROUP BY YEAR(o.paidAt), MONTH(o.paidAt)
        ORDER BY YEAR(o.paidAt), MONTH(o.paidAt)
    """)
    List<Object[]> revenueByMonth(LocalDateTime start);
}

