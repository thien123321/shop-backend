package com.minhthien.web.shop.repository.product;

import com.minhthien.web.shop.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    List<Product> id(Long id);

    Page<Product> findByNameContainingIgnoreCase(
            String keyword,
            Pageable pageable
    );
}
