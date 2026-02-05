package com.minhthien.web.shop.dto.category;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CategoryTreeResponseDTO {

    private Long id;
    private String name;
    private Long parentId;
    private List<CategoryTreeResponseDTO> children;
}
