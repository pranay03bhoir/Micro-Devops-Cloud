package com.pranay.easybuy.products.services;

import com.pranay.easybuy.products.dto.CategoryDTO;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    List<CategoryDTO> getAllCategories();

    CategoryDTO getCategoryById(Long categoryId);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);

    String deleteCategory(Long categoryId);

    List<CategoryDTO> getCategoriesByProductId(UUID productId);
}
