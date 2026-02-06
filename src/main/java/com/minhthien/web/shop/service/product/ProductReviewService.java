package com.minhthien.web.shop.service.product;

import com.minhthien.web.shop.dto.product.ProductReviewRequest;
import com.minhthien.web.shop.dto.product.ProductReviewResponse;
import com.minhthien.web.shop.entity.Auth.User;
import com.minhthien.web.shop.entity.product.Product;
import com.minhthien.web.shop.entity.product.ProductReview;
import com.minhthien.web.shop.repository.product.ProductRepository;
import com.minhthien.web.shop.repository.product.ProductReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductReviewService {

    private final ProductReviewRepository reviewRepo;
    private final ProductRepository productRepo;

    public ProductReviewResponse reviewProduct(
            Long userId,
            ProductReviewRequest req
    ) {

        if (reviewRepo
                .findByProductIdAndUserId(req.getProductId(), userId)
                .isPresent()) {
            throw new RuntimeException("You already reviewed");
        }

        Product product = productRepo
                .findById(req.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // save review
        ProductReview review = ProductReview.builder()
                .product(product)
                .user(User.builder().id(userId).build())
                .rating(req.getRating())
                .comment(req.getComment())
                .createdAt(LocalDateTime.now())
                .build();

        reviewRepo.save(review);

        // ===== tính lại rating =====
        Double avg = reviewRepo.avgRating(product.getId());
        Long total = reviewRepo.totalReviews(product.getId());

        product.setAvgRating(avg);
        product.setTotalReviews(total.intValue());
        productRepo.save(product);

        // ===== response =====
        return ProductReviewResponse.builder()
                .message("Reviewed")
                .rating(req.getRating())
                .avgRating(avg)
                .totalReviews(total.intValue())
                .build();
    }
    public List<ProductReview> getReviews(Long productId) {

        // check product tồn tại
        productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return reviewRepo.findByProductId(productId);
    }
}
