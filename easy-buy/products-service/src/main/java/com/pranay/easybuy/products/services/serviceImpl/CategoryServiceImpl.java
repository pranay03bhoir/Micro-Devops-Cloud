package com.pranay.easybuy.products.services.serviceImpl;

import com.pranay.easybuy.products.config.CategoryMapper;
import com.pranay.easybuy.products.dto.CategoryDTO;
import com.pranay.easybuy.products.exceptions.ResourceNotFoundException;
import com.pranay.easybuy.products.models.Category;
import com.pranay.easybuy.products.models.Product;
import com.pranay.easybuy.products.repositories.CategoryRepository;
import com.pranay.easybuy.products.repositories.ProductRepository;
import com.pranay.easybuy.products.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryMapper categoryMapper;
	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;

	@Override
	public CategoryDTO createCategory(CategoryDTO categoryDTO) {
		Category category = categoryMapper.toEntity(categoryDTO);
		Category savedCategory = categoryRepository.save(category);
		return categoryMapper.toDto(savedCategory);
	}

	@Override
	public List<CategoryDTO> getAllCategories() {
		return categoryMapper.toDtoList(categoryRepository.findAll());
	}

	@Override
	public CategoryDTO getCategoryById(Long categoryId) {
		Category category = findCategoryById(categoryId);
		return categoryMapper.toDto(category);
	}

	@Override
	public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
		Category category = findCategoryById(categoryId);
		category.setTitle(categoryDTO.getTitle());
		return categoryMapper.toDto(category);
	}

	@Override
	public String deleteCategory(Long categoryId) {
		categoryRepository.deleteById(categoryId);
		return "Category deleted successfully!!!";
	}

	@Override
	public List<CategoryDTO> getCategoriesByProductId(UUID productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found!!!"));
		List<Category> categories = categoryRepository.findCategoriesByProductId(product.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Categories not found!!!"));
		return categoryMapper.toDtoList(categories);
	}

	private Category findCategoryById(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found!!!"));
		return category;
	}
}
