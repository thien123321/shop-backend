package com.minhthien.web.shop.service.pay;

import com.fasterxml.jackson.databind.JsonNode;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookService {

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    @Value("${payos.checksum-key}")
    private String checksumKey;

    @Transactional
    public void handle(String rawBody) {

        try {

            // ===== Parse JSON =====
            JsonNode root =
                    objectMapper.readTree(rawBody);

            String signature =
                    root.get("signature").asText();

            // ===== Convert data → Map =====
            Map<String, Object> dataMap =
                    objectMapper.convertValue(
                            root.get("data"),
                            Map.class
                    );

            // ===== Verify signature =====
            boolean valid =
                    PayOSWebhookVerifier.verify(
                            dataMap,
                            signature,
                            checksumKey
                    );

            if (!valid) {
                log.warn("❌ Invalid PayOS signature");
                log.info("DATA MAP = {}", dataMap);
                log.info("SIGN = {}", signature);
                return; // ❌ Không update order
            }

            // ===== Map DTO =====
            PayOSWebhookRequest request =
                    objectMapper.treeToValue(
                            root,
                            PayOSWebhookRequest.class
                    );

            Long orderCode =
                    request.getData().getOrderCode();

            Order order =
                    orderRepository
                            .findByOrderCode(orderCode)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Order not found: "
                                                    + orderCode
                                    )
                            );

            // ===== Idempotent check =====
            if (order.getStatus()
                    == OrderStatus.PAID) {

                log.info(
                        "⚠️ Order {} already PAID → skip",
                        order.getId()
                );
                return;
            }

            // ===== Check amount =====
            BigDecimal webhookAmount =
                    BigDecimal.valueOf(
                            request.getData()
                                    .getAmount()
                    );

            if (order.getAmount()
                    .compareTo(webhookAmount) != 0) {

                log.error(
                        "❌ Amount mismatch | order={} | db={} | webhook={}",
                        order.getId(),
                        order.getAmount(),
                        webhookAmount
                );

                order.setStatus(OrderStatus.FAILED);
                orderRepository.save(order);
                return;
            }

            // ===== SUCCESS =====
            if ("00".equals(request.getCode())
                    && Boolean.TRUE.equals(
                    request.getSuccess())) {

                order.setStatus(OrderStatus.PAID);
                order.setPaidAt(LocalDateTime.now());

                orderRepository.save(order);

                log.info(
                        "✅ Order {} PAID",
                        order.getId()
                );
            }

            // ===== FAILED =====
            else {

                order.setStatus(OrderStatus.FAILED);

                orderRepository.save(order);

                log.warn(
                        "❌ Order {} FAILED | code={} | desc={}",
                        order.getId(),
                        request.getCode(),
                        request.getDesc()
                );
            }

        } catch (Exception e) {

            log.error("❌ Webhook error", e);

            throw new RuntimeException(
                    "Webhook error", e
            );
        }
    }
}
