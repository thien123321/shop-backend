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
    WHERE o.createdAt >= :start
      AND o.createdAt < :end
""")
    Long countOrdersToday(LocalDateTime start,
                          LocalDateTime end);

    // ===== REVENUE TODAY =====
    @Query("""
    SELECT COALESCE(SUM(o.amount),0)
    FROM Order o
    WHERE o.status = 'PAID'
      AND o.paidAt >= :start
      AND o.paidAt < :end
""")
    BigDecimal revenueToday(LocalDateTime start,
                            LocalDateTime end);

    // ===== CHART BY HOUR =====
    @Query("""
    SELECT function('hour', o.paidAt), SUM(o.amount)
    FROM Order o
    WHERE o.status = 'PAID'
      AND o.paidAt >= :start
    GROUP BY function('hour', o.paidAt)
    ORDER BY function('hour', o.paidAt)
""")
    List<Object[]> revenueByHour(LocalDateTime start);

    // ===== CHART BY DAY =====
    @Query("""
        SELECT function('date', o.paidAt), SUM(o.amount)
        FROM Order o
        WHERE o.status = 'PAID'
          AND o.paidAt >= :start
        GROUP BY DATE(o.paidAt)
        ORDER BY DATE(o.paidAt)
    """)
    List<Object[]> revenueByDay(LocalDateTime start);

    // ===== CHART BY MONTH =====
    @Query("""
        SELECT function('year', o.paidAt),
               function('month', o.paidAt), SUM(o.amount)
        FROM Order o
        WHERE o.status = 'PAID'
          AND o.paidAt >= :start
        GROUP BY YEAR(o.paidAt), MONTH(o.paidAt)
        ORDER BY YEAR(o.paidAt), MONTH(o.paidAt)
    """)
    List<Object[]> revenueByMonth(LocalDateTime start);
}

