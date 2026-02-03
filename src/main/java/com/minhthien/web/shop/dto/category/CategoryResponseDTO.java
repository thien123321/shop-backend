package com.minhthien.web.shop.dto.category;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryResponseDTO {

    private Long id;
    private String name;
    private String description;
}
