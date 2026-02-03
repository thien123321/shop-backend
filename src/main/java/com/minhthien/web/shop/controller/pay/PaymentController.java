package com.minhthien.web.shop.controller.pay;

import com.minhthien.web.shop.dto.pay.BuyNowRequest;
import com.minhthien.web.shop.dto.pay.CreatePaymentResponse;
import com.minhthien.web.shop.entity.Auth.User;
import com.minhthien.web.shop.entity.pay.Order;
import com.minhthien.web.shop.entity.product.Product;
import com.minhthien.web.shop.service.pay.OrderService;
import com.minhthien.web.shop.service.pay.PaymentService;
import com.minhthien.web.shop.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final ProductService productService;

    @PostMapping("/create/{orderId}")
    @PreAuthorize("isAuthenticated()") // bắt buộc login
    public ResponseEntity<CreatePaymentResponse> createPayment(
            @PathVariable Long orderId
    ) throws Exception {
        CreatePaymentResponse response = paymentService.createPayment(orderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orther/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }

    @PostMapping("/buy-now")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> buyNow(
            @RequestBody BuyNowRequest request,
            @AuthenticationPrincipal User user
    ) throws Exception {

        Product product = productService.getById(request.getProductId());

        Order order = orderService.createBuyNowOrder(
                user,
                product,
                request.getQuantity()
        );

        CreatePaymentResponse payment = paymentService.createPayment(order.getId());
        return ResponseEntity.ok(Map.of(
                "orderId", order.getId(),
                "paymentUrl", payment.getPaymentUrl()
        ));
    }

}
