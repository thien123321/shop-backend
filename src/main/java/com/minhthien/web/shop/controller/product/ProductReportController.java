package com.minhthien.web.shop.controller.product;

import com.minhthien.web.shop.dto.product.ProductReportRequest;
import com.minhthien.web.shop.dto.product.ProductReportResponse;
import com.minhthien.web.shop.service.product.ProductReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product-report")
@RequiredArgsConstructor
public class ProductReportController {

    private final ProductReportService reportService;

    @PostMapping
    public ResponseEntity<ProductReportResponse> report(
            @RequestBody ProductReportRequest req,
            @RequestHeader("userId") Long userId
    ) {
        return ResponseEntity.ok(
                reportService.reportProduct(userId, req)
        );
    }
}