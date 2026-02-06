package com.minhthien.web.shop.repository.product;

import com.minhthien.web.shop.entity.product.ProductReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReportRepository extends JpaRepository<ProductReport, Long> {
    long countByProductId(Long productId);
}
