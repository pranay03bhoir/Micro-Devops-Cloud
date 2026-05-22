package com.pranay.easybuy.products.config;

import com.pranay.easybuy.products.dto.CategoryDTO;
import com.pranay.easybuy.products.models.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

	// To DTO
	@Mapping(target = "createdAt", source = "createdAt")
	@Mapping(target = "updatedAt", source = "updatedAt")
	CategoryDTO toDto(Category category);

	// To Entity
	@Mapping(target = "createdAt", source = "createdAt")
	@Mapping(target = "updatedAt", source = "updatedAt")
	Category toEntity(CategoryDTO categoryDTO);

	// Category List to DTO
	List<CategoryDTO> toDtoList(List<Category> categories);

	// Category List DTO to entity
	List<Category> toEntityList(List<CategoryDTO> categoryDTOS);
}
