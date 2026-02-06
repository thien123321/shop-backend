package com.minhthien.web.shop.service.product;


import com.minhthien.web.shop.dto.product.ProductFindResponse;
import com.minhthien.web.shop.dto.product.ProductInsertRequest;
import com.minhthien.web.shop.dto.product.ProductInsertResponse;
import com.minhthien.web.shop.entity.category.Category;
import com.minhthien.web.shop.entity.product.Product;
import com.minhthien.web.shop.enums.ProductStatus;
import com.minhthien.web.shop.repository.product.ProductRepository;
import com.minhthien.web.shop.service.upload.ImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ImageStorageService imageStorageService;


    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product getById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found: " ));
    }

    public Page<ProductFindResponse> getProducts(
            int page,
            int size,
            String sortBy,
            String keyword
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortBy).ascending()
        );

        Page<Product> productPage;

        if (keyword == null || keyword.trim().isEmpty()) {
            productPage = productRepository.findAll(pageable);
        } else {
            productPage = productRepository
                    .findByNameContainingIgnoreCase(keyword, pageable);
        }

        return productPage.map(this::toResponse);
    }
    private ProductFindResponse toResponse(Product product) {
        return ProductFindResponse.builder()
                .productId(product.getId())
                .productName(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .status(product.getStatus())
                .build();
    }

    public ProductInsertResponse insertProduct(
            String name,
            String description,
            BigDecimal price,
            Long categoryId,
            MultipartFile image,
            Integer stock
    ) {
        if (productRepository.existsByName(name)) {
            throw new RuntimeException("Product name already exists");
        }

        String fileName = imageStorageService.storeFile(image);
        String imageUrl = "http://localhost:8080/upload/" + fileName;

        Category category = Category.builder()
                .categoryId(categoryId)
                .build();

        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .imageUrl(imageUrl)
                .category(category)
                .stock(stock)
                .build();

        Product saved = productRepository.save(product);

        return ProductInsertResponse.builder()
                .productId(saved.getId())
                .productName(saved.getName())
                .productDescription(saved.getDescription())
                .productPrice(saved.getPrice())
                .categoryId(categoryId)
                .imageUrl(saved.getImageUrl())
                .stock(saved.getStock())
                .build();
    }

    public ProductInsertResponse updateProduct(
            Long id,
            String name,
            String description,
            BigDecimal price,
            MultipartFile image,
            Integer stock
    ) {
        if (productRepository.existsByNameAndIdNot(name,id)) {
            throw new RuntimeException("Product id already exists");
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);

        // Nếu có ảnh mới thì xoá ảnh cũ
        if (image != null && !image.isEmpty()) {

            if (product.getImageUrl() != null) {
                String oldFile = product.getImageUrl()
                        .substring(product.getImageUrl().lastIndexOf("/") + 1);
                imageStorageService.deleteFile(oldFile);
            }

            String newFileName = imageStorageService.storeFile(image);
            String newUrl = "http://localhost:8080/upload/" + newFileName;
            product.setImageUrl(newUrl);
        }

        Product saved = productRepository.save(product);

        return ProductInsertResponse.builder()
                .productId(saved.getId())
                .productName(saved.getName())
                .productDescription(saved.getDescription())
                .productPrice(saved.getPrice())
                .imageUrl(saved.getImageUrl())
                .stock(saved.getStock())
                .build();
    }



    public void deleteProduct(Long id) {
        Product products = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        if (products.getImageUrl() != null && !products.getImageUrl().isEmpty()) {
            String fileName = products.getImageUrl()
                    .substring(products.getImageUrl().lastIndexOf("/") + 1);
            imageStorageService.deleteFile(fileName);
        }

        // xoá product khỏi DB
        productRepository.delete(products);


    }


}
