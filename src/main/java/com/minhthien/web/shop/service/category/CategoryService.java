package com.minhthien.web.shop.service.category;

import com.minhthien.web.shop.dto.category.*;
import com.minhthien.web.shop.entity.category.Category;
import com.minhthien.web.shop.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // ================= GET ALL =================
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ================= CREATE =================
    public CategoryResponseDTO createCategory(CategoryRequestDTO request) {

        if (categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("Category already exists");
        }

        Category parent = null;

        // Nếu có parentId → tìm cha
        if (request.getParentId() != null) {
            parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
        }

        Category category = Category.builder()
                .name(request.getName())
                .parent(parent) // set parent
                .build();

        return toResponse(categoryRepository.save(category));
    }

    // ================= UPDATE =================
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(request.getName());

        if (request.getParentId() != null) {

            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent not found"));

            if (parent.getCategoryId().equals(id)) {
                throw new RuntimeException("Cannot set itself as parent");
            }

            category.setParent(parent);

        } else {
            category.setParent(null);
        }

        return toResponse(categoryRepository.save(category));
    }

    // ================= DELETE =================
    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        categoryRepository.delete(category);
    }

    // ================= TREE =================
    @Transactional(readOnly = true)
    public List<CategoryTreeResponseDTO> getCategoryTree() {

        return categoryRepository.findByParentIsNull()
                .stream()
                .map(this::mapToTree)
                .toList();
    }

    // ================= MAPPERS =================

    private CategoryResponseDTO toResponse(Category category) {
        return CategoryResponseDTO.builder()
                .id(category.getCategoryId())
                .name(category.getName())
                .parentId(
                        category.getParent() != null
                                ? category.getParent().getCategoryId()
                                : null
                )
                .build();
    }

    private CategoryTreeResponseDTO mapToTree(Category category) {

        return CategoryTreeResponseDTO.builder()
                .id(category.getCategoryId())
                .name(category.getName())
                .parentId(
                        category.getParent() != null
                                ? category.getParent().getCategoryId()
                                : null
                )
                .children(
                        category.getChildren() != null
                                ? category.getChildren()
                                .stream()
                                .map(this::mapToTree)
                                .toList()
                                : List.of()
                )
                .build();
    }
}
