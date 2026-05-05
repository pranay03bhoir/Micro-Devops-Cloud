package com.substring.blogapp.service;

import com.substring.blogapp.dto.CategoryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId);

    void deleteCategory(Long categoryId);

    List<CategoryDto> fetchAllCategories();

    CategoryDto getACategoryById(Long categoryId);
}
