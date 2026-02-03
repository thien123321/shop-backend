package com.minhthien.web.shop.service.pay;

import com.minhthien.web.shop.entity.pay.Order;
import com.minhthien.web.shop.entity.pay.Refund;
import com.minhthien.web.shop.enums.OrderStatus;
import com.minhthien.web.shop.enums.RefundStatus;
import com.minhthien.web.shop.repository.pay.OrderRepository;
import com.minhthien.web.shop.repository.pay.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final OrderRepository orderRepository;
    private final RefundRepository refundRepository;

    public Refund requestRefund(Long orderId, String reason) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow();

        if (order.getStatus() != OrderStatus.PAID) {
            throw new RuntimeException("Order not eligible for refund");
        }

        Refund refund = Refund.builder()
                .orderId(order.getId())
                .amount(order.getAmount())
                .reason(reason)
                .build();

        return refundRepository.save(refund);
    }

    // Admin confirm đã hoàn tiền
    public void completeRefund(Long refundId) {

        Refund refund = refundRepository.findById(refundId)
                .orElseThrow();

        Order order = orderRepository.findById(refund.getOrderId())
                .orElseThrow();

        refund.setStatus(RefundStatus.COMPLETED);
        refund.setCompletedAt(LocalDateTime.now());

        order.setStatus(OrderStatus.REFUNDED);
        order.setRefundedAt(LocalDateTime.now());

        refundRepository.save(refund);
        orderRepository.save(order);
    }
}
