package com.minhthien.web.shop.service.pay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhthien.web.shop.config.payos.PayOSWebhookVerifier;
import com.minhthien.web.shop.dto.pay.PayOSWebhookRequest;
import com.minhthien.web.shop.entity.pay.Order;
import com.minhthien.web.shop.enums.OrderStatus;
import com.minhthien.web.shop.repository.pay.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookService {

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    @Value("${payos.checksum-key}")
    private String checksumKey;


    @Transactional
    public void handle(String rawBody, String signature) {

        boolean valid = PayOSWebhookVerifier.verify(
                rawBody,
                signature,
                checksumKey
        );

        if (!valid) {
            log.warn("❌ Invalid PayOS signature");
            return;
        }


        PayOSWebhookRequest request;
        try {
            request = objectMapper.readValue(rawBody, PayOSWebhookRequest.class);
        } catch (Exception e) {
            throw new RuntimeException("Invalid payload");
        }

        if (!"PAID".equals(request.getData().getStatus())) return;

        Order order = orderRepository
                .findByOrderCode(request.getData().getOrderCode())
                .orElseThrow();

        if (order.getStatus() == OrderStatus.PAID) {
            log.info("Webhook duplicate for order {}", order.getId());
            return;
        }


        if (order.getAmount().longValue() != request.getData().getAmount()) {
            log.error("❌ Amount mismatch: order={}, webhook={}",
                    order.getAmount(), request.getData().getAmount());
            return;

        }

        order.setStatus(OrderStatus.PAID);
        order.setPaidAt(LocalDateTime.now());
        orderRepository.save(order);

        log.info("✅ Order {} PAID", order.getId());
    }
}
