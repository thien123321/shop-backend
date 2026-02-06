package com.minhthien.web.shop.repository.product;

import com.minhthien.web.shop.entity.product.ProductReview;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductReviewRepository
        extends JpaRepository<ProductReview, Long> {

    Optional<ProductReview>
    findByProductIdAndUserId(Long productId, Long userId);

    List<ProductReview> findByProductId(Long productId);

    @Query("""
        SELECT AVG(r.rating)
        FROM ProductReview r
        WHERE r.product.id = :productId
    """)
    Double avgRating(@Param("productId") Long productId);

    @Query("""
        SELECT COUNT(r)
        FROM ProductReview r
        WHERE r.product.id = :productId
    """)
    Long totalReviews(@Param("productId") Long productId);
}
