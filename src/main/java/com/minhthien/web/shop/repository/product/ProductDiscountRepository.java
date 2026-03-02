package com.minhthien.web.shop.repository.product;

import com.minhthien.web.shop.entity.product.ProductDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductDiscountRepository extends JpaRepository<ProductDiscount, Long> {
    List<ProductDiscount> findByProductId(Long productId);

    @Query("""
    SELECT d FROM ProductDiscount d
    WHERE d.productId = :productId
    AND d.active = true
    AND (
        (d.startAt IS NULL OR d.startAt <= :now)
        AND
        (d.endAt IS NULL OR d.endAt >= :now)
    )
""")
    List<ProductDiscount> findAllActive(
            @Param("productId") Long productId,
            @Param("now") LocalDateTime now
    );
}
