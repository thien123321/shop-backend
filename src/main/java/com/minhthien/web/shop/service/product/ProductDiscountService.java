package com.minhthien.web.shop.service.product;

import com.minhthien.web.shop.dto.product.ProductDiscountRequest;
import com.minhthien.web.shop.dto.product.ProductDiscountResponse;
import com.minhthien.web.shop.entity.product.ProductDiscount;
import com.minhthien.web.shop.repository.product.ProductDiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDiscountService {

    private final ProductDiscountRepository repo;

    // ================= CREATE =================

    public ProductDiscountResponse create(
            ProductDiscountRequest req
    ) {

        validate(req);

        ProductDiscount d = ProductDiscount.builder()
                .productId(req.getProductId())
                .discountPercent(req.getDiscountPercent())
                .discountAmount(req.getDiscountAmount())
                .startAt(req.getStartAt())
                .endAt(req.getEndAt())
                .active(true)
                .build();

        return toResponse(repo.save(d));
    }

    // ================= UPDATE =================

    public ProductDiscountResponse update(
            Long id,
            ProductDiscountRequest req
    ) {

        ProductDiscount d =
                repo.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Discount not found"));

        validate(req);

        d.setDiscountPercent(req.getDiscountPercent());
        d.setDiscountAmount(req.getDiscountAmount());
        d.setStartAt(req.getStartAt());
        d.setEndAt(req.getEndAt());

        return toResponse(repo.save(d));
    }

    // ================= DELETE =================

    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ================= GET BY PRODUCT =================

    public List<ProductDiscount> getByProduct(Long productId) {
        return repo.findByProductId(productId);
    }

    // ================= CALCULATE DISCOUNT =================
    // Logic "song song + đè nhau"

    public BigDecimal calculateDiscount(
            Long productId,
            BigDecimal price
    ) {

        List<ProductDiscount> discounts =
                repo.findAllActive(
                        productId,
                        LocalDateTime.now()
                );

        if (discounts.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal maxDiscount = BigDecimal.ZERO;

        for (ProductDiscount d : discounts) {

            BigDecimal discount = BigDecimal.ZERO;

            // ===== % DISCOUNT =====
            if (d.getDiscountPercent() != null) {

                BigDecimal percent =
                        BigDecimal.valueOf(d.getDiscountPercent())
                                .divide(BigDecimal.valueOf(100));

                discount = price.multiply(percent);
            }

            // ===== AMOUNT DISCOUNT =====
            if (d.getDiscountAmount() != null) {

                // Lấy cái lớn hơn giữa % và amount
                discount =
                        discount.max(d.getDiscountAmount());
            }

            // ===== KHÔNG CHO > PRICE =====
            if (discount.compareTo(price) > 0) {
                discount = price;
            }

            // ===== LẤY MAX GIỮA CÁC DISCOUNT =====
            if (discount.compareTo(maxDiscount) > 0) {
                maxDiscount = discount;
            }
        }

        return maxDiscount;
    }

    // ================= VALIDATE =================

    private void validate(ProductDiscountRequest req) {

        if (req.getDiscountPercent() == null
                && req.getDiscountAmount() == null) {

            throw new RuntimeException(
                    "Must have percent or amount discount"
            );
        }

        if (req.getDiscountPercent() != null
                && req.getDiscountPercent() <= 0) {

            throw new RuntimeException(
                    "Percent must > 0"
            );
        }

        if (req.getDiscountAmount() != null
                && req.getDiscountAmount()
                .compareTo(BigDecimal.ZERO) <= 0) {

            throw new RuntimeException(
                    "Amount must > 0"
            );
        }

        if (req.getStartAt() != null
                && req.getEndAt() != null
                && req.getStartAt().isAfter(req.getEndAt())) {

            throw new RuntimeException(
                    "Start date must before end date"
            );
        }
    }


    // ================= MAPPER =================

    private ProductDiscountResponse toResponse(
            ProductDiscount d
    ) {
        return ProductDiscountResponse.builder()
                .id(d.getId())
                .productId(d.getProductId())
                .discountPercent(d.getDiscountPercent())
                .discountAmount(d.getDiscountAmount())
                .startAt(d.getStartAt())
                .endAt(d.getEndAt())
                .active(d.getActive())
                .build();
    }
}
