package com.substring.blogapp.service.serviceImpl;

import com.substring.blogapp.dto.CategoryDto;
import com.substring.blogapp.exceptions.ResourceNotFoundException;
import com.substring.blogapp.models.Category;
import com.substring.blogapp.repositories.CategoryRepository;
import com.substring.blogapp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category createdCategory = modelMapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(createdCategory);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        category.setName(categoryDto.getName());
        categoryRepository.save(category);
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public List<CategoryDto> fetchAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new ResourceNotFoundException("Now categories are present");
        }
        List<CategoryDto> categoryDtoList = categories.stream().map((category -> modelMapper.map(category, CategoryDto.class))).toList();
        return categoryDtoList;
    }

    @Override
    public CategoryDto getACategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("The category provided does not exists."));
        return modelMapper.map(category, CategoryDto.class);
    }
}
