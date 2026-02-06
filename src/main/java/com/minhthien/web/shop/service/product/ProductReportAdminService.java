package com.minhthien.web.shop.service.product;

import com.minhthien.web.shop.dto.product.AdminProductReportResponse;
import com.minhthien.web.shop.entity.product.ProductReport;
import com.minhthien.web.shop.repository.product.AdminProductReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class ProductReportAdminService {
    private final AdminProductReportRepository reportRepo;

    // ===== GET ALL REPORT =====
    public List<AdminProductReportResponse> getAll() {

        return reportRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ===== GET BY PRODUCT =====
    public List<AdminProductReportResponse>
    getByProduct(Long productId) {

        return reportRepo
                .findByProductId(productId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ===== GET BY STATUS =====
    public List<AdminProductReportResponse>
    getByStatus(String status) {

        return reportRepo
                .findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ===== MAPPER =====
    private AdminProductReportResponse
    mapToResponse(ProductReport r) {

        return AdminProductReportResponse.builder()
                .id(r.getId())
                .productId(r.getProduct().getId())
                .productName(r.getProduct().getName())
                .userId(r.getUser().getId())
                .reason(r.getReason())
                .description(r.getDescription())
                .status(r.getStatus())
                .createdAt(r.getCreatedAt())
                .build();
    }
}
