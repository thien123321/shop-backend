package com.minhthien.web.shop.controller.product;

import com.minhthien.web.shop.dto.product.ProductDiscountRequest;
import com.minhthien.web.shop.dto.product.ProductDiscountResponse;
import com.minhthien.web.shop.entity.product.ProductDiscount;
import com.minhthien.web.shop.service.product.ProductDiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-discount")
@RequiredArgsConstructor
public class ProductDiscountController {

    private final ProductDiscountService service;

    // CREATE
    @PostMapping
    public ProductDiscountResponse create(
            @RequestBody ProductDiscountRequest req
    ) {
        return service.create(req);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ProductDiscountResponse update(
            @PathVariable Long id,
            @RequestBody ProductDiscountRequest req
    ) {
        return service.update(id, req);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // GET BY PRODUCT
    @GetMapping("/product/{productId}")
    public List<ProductDiscount> getByProduct(
            @PathVariable Long productId
    ) {
        return service.getByProduct(productId);
    }
}
