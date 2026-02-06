package com.minhthien.web.shop.service.product;

import com.minhthien.web.shop.dto.product.ProductReportRequest;
import com.minhthien.web.shop.dto.product.ProductReportResponse;
import com.minhthien.web.shop.entity.Auth.User;
import com.minhthien.web.shop.entity.product.Product;
import com.minhthien.web.shop.entity.product.ProductReport;
import com.minhthien.web.shop.repository.product.ProductReportRepository;
import com.minhthien.web.shop.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductReportService {

    private final ProductReportRepository reportRepo;
    private final ProductRepository productRepo;

    public ProductReportResponse reportProduct(
            Long userId,
            ProductReportRequest req
    ) {

        Product product = productRepo
                .findById(req.getProductId())
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        ProductReport report = ProductReport.builder()
                .product(product)
                .user(User.builder().id(userId).build())
                .reason(req.getReason())
                .description(req.getDescription())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();

        reportRepo.save(report);

        // Đếm tổng report
        long total = reportRepo
                .countByProductId(product.getId());

        return ProductReportResponse.builder()
                .message("Reported")
                .reason(req.getReason())
                .totalReports(total)
                .build();
    }
}