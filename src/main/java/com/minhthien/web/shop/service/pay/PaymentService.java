package com.minhthien.web.shop.service.pay;

import com.minhthien.web.shop.dto.pay.CreatePaymentResponse;
import com.minhthien.web.shop.entity.pay.Order;
import com.minhthien.web.shop.enums.OrderStatus;
import com.minhthien.web.shop.repository.pay.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;

@Service
@RequiredArgsConstructor
public class PaymentService {


    private final PayOS payOS;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @Value("${payos.return-url}")
    private String returnUrl;

    @Value("${payos.cancel-url}")
    private String cancelUrl;

    @Transactional
    public CreatePaymentResponse createPayment(Long orderId) throws Exception {

        Order order = orderService.getById(orderId);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Order not available for payment");
        }

        CreatePaymentLinkRequest request =
                CreatePaymentLinkRequest.builder()
                        .orderCode(order.getOrderCode())
                        .amount(order.getAmount().longValue())
                        .description("Thanh toán đơn #" + order.getId())
                        .returnUrl(returnUrl)
                        .cancelUrl(cancelUrl)
                        .build();

        var response = payOS.paymentRequests().create(request);

        return CreatePaymentResponse.builder()
                .orderId(order.getId())
                .paymentId(order.getOrderCode())
                .status(order.getStatus().name())
                .paymentUrl(response.getCheckoutUrl())
                .build();
    }
}
