package com.minhthien.web.shop.controller.product;

import com.minhthien.web.shop.dto.product.ProductReviewRequest;
import com.minhthien.web.shop.dto.product.ProductReviewResponse;
import com.minhthien.web.shop.service.product.ProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product-review")
@RequiredArgsConstructor
public class ProductReviewController {

    private final ProductReviewService reviewService;

    @PostMapping
    public ResponseEntity<ProductReviewResponse> review(
            @RequestBody ProductReviewRequest req,
            @RequestHeader("userId") Long userId
    ) {
        return ResponseEntity.ok(
                reviewService.reviewProduct(userId, req)
        );
    }

    // ===== LIST REVIEW =====
    @GetMapping("/{productId}")
    public ResponseEntity<?> getReviews(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(
                reviewService.getReviews(productId)
        );
    }
}
