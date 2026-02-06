package com.minhthien.web.shop.controller.product;

import com.minhthien.web.shop.dto.product.ProductFindResponse;
import com.minhthien.web.shop.dto.product.ProductInsertResponse;
import com.minhthien.web.shop.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

    @PreAuthorize("hasAnyRole('USER','ADMIN','STAFF')")
    @GetMapping("/findproduct")
    public ResponseEntity<Page<ProductFindResponse>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(
                productService.getProducts(page, size, sortBy, keyword)
        );
    }



    @PostMapping( value ="/insert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductInsertResponse> insertProduct(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam BigDecimal price,
            @RequestParam Long categoryId,
            @RequestParam MultipartFile image,
            @RequestParam Integer stock
    ) {
        return ResponseEntity.ok(
                productService.insertProduct(name,description,price,categoryId,image,stock)
        );
    }


    @PutMapping(value ="/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductInsertResponse> updateProduct(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam BigDecimal price,
            @RequestParam Integer stock,
            @RequestParam(required = false) MultipartFile image
    ) {
        return ResponseEntity.ok(
                productService.updateProduct(id, name, description, price,image,stock)
        );
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {

        productService.deleteProduct(id);
        return ResponseEntity.ok("Deleted");
    }

}
