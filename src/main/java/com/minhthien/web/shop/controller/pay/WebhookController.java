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
            @RequestBody String rawBody
    ) {
        webhookService.handle(rawBody);
        return ResponseEntity.ok("OK");
    }
}
