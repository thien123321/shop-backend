package com.minhthien.web.shop.repository.product;

import com.minhthien.web.shop.entity.product.ProductReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminProductReportRepository extends JpaRepository<ProductReport, Long> {
    List<ProductReport> findByProductId(Long productId);

    List<ProductReport> findByStatus(String status);
}
