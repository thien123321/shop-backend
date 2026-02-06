package com.minhthien.web.shop.controller.admin;

import com.minhthien.web.shop.dto.product.AdminProductReportResponse;
import com.minhthien.web.shop.dto.product.ProductReportResponse;
import com.minhthien.web.shop.service.product.ProductReportAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/product-reports")
@RequiredArgsConstructor
public class ProductReportAdminController {

    private final ProductReportAdminService adminService;

    // ===== ALL REPORT =====
    @GetMapping
    public ResponseEntity<List<AdminProductReportResponse>>
    getAll() {

        return ResponseEntity.ok(
                adminService.getAll()
        );
    }

    // ===== REPORT BY PRODUCT =====
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<AdminProductReportResponse>>
    getByProduct(
            @PathVariable Long productId
    ) {

        return ResponseEntity.ok(
                adminService.getByProduct(productId)
        );
    }

    // ===== REPORT BY STATUS =====
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AdminProductReportResponse>>
    getByStatus(
            @PathVariable String status
    ) {

        return ResponseEntity.ok(
                adminService.getByStatus(status)
        );
    }
}
