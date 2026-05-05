package com.pranay.easybuy.products.config;

import com.pranay.easybuy.products.dto.CategoryDTO;
import com.pranay.easybuy.products.models.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    //    To DTO
    CategoryDTO toDto(Category category);

    //    To Entity
    Category toEntity(CategoryDTO categoryDTO);

    //    Category List to DTO
    List<CategoryDTO> toDtoList(List<Category> categories);

    //    Category List DTO to entity
    List<Category> toEntityList(List<CategoryDTO> categoryDTOS);
}
