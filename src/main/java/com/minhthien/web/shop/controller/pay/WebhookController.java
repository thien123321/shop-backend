package com.minhthien.web.shop.controller.pay;

import com.minhthien.web.shop.service.pay.WebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payos/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService webhookService;

    @PostMapping
    public ResponseEntity<String> webhook(
            @RequestBody String rawBody,
            @RequestHeader(value = "x-payos-signature", required = false)
            String signature
    ) {
        webhookService.handle(rawBody, signature);
        return ResponseEntity.ok("OK");
    }
}
