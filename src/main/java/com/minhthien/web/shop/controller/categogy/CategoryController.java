package com.minhthien.web.shop.controller.categogy;

import com.minhthien.web.shop.dto.category.*;
import com.minhthien.web.shop.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // ===== GET ALL =====
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAll() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    // ===== CREATE =====
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(
            @RequestBody @Valid CategoryRequestDTO request
    ) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    // ===== UPDATE =====
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid CategoryRequestDTO request
    ) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    // ===== DELETE =====
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    // ===== TREE =====
    @GetMapping("/tree")
    public ResponseEntity<List<CategoryTreeResponseDTO>> getTree() {
        return ResponseEntity.ok(categoryService.getCategoryTree());
    }
}
