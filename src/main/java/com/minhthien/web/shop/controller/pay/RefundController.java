package com.minhthien.web.shop.controller.pay;

import com.minhthien.web.shop.service.pay.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    // User gọi
    @PostMapping("/request")
    public ResponseEntity<?> requestRefund(
            @RequestParam Long orderId,
            @RequestParam String reason
    ) {
        return ResponseEntity.ok(
                refundService.requestRefund(orderId, reason)
        );
    }

    // Admin gọi
    @PostMapping("/{id}/complete")
    public ResponseEntity<?> complete(@PathVariable Long id) {
        refundService.completeRefund(id);
        return ResponseEntity.ok("REFUNDED");
    }
}
