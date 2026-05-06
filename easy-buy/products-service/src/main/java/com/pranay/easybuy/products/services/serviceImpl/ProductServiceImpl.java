package com.pranay.easybuy.products.services.serviceImpl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pranay.easybuy.products.config.CategoryMapper;
import com.pranay.easybuy.products.config.ProductMapper;
import com.pranay.easybuy.products.dto.ProductDTO;
import com.pranay.easybuy.products.exceptions.ResourceNotFoundException;
import com.pranay.easybuy.products.models.Category;
import com.pranay.easybuy.products.models.Product;
import com.pranay.easybuy.products.repositories.CategoryRepository;
import com.pranay.easybuy.products.repositories.ProductRepository;
import com.pranay.easybuy.products.services.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final ProductMapper productMapper;
	private final CategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;

	@Override
	public ProductDTO createProduct(ProductDTO productDTO) {
		Product product = productMapper.toEntity(productDTO);
		Product savedProduct = productRepository.save(product);
		return productMapper.toDto(savedProduct);
	}

	@Override
	public List<ProductDTO> getAllProducts() {
		return productMapper.toDtoList(productRepository.findAll());
	}

	@Override
	public ProductDTO getProductById(UUID productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found!!!"));
		return productMapper.toDto(product);
	}

	@Override
	public ProductDTO updateProduct(ProductDTO productDTO, UUID productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found!!!"));
		product.setTitle(productDTO.getTitle());
		product.setShort_desc(productDTO.getShortDesc());
		product.setLong_desc(productDTO.getLongDesc());
		product.setPrice(productDTO.getPrice());
		product.setDiscount(productDTO.getDiscount());
		product.setProductImages(productDTO.getProductImages());
		product.setLive(productDTO.getLive());
		return productMapper.toDto(product);
	}

	@Override
	public String deleteProduct(UUID productId) {
		productRepository.deleteById(productId);
		return "Product deleted successfully";
	}

	@Override
	public List<ProductDTO> getProductsByCategory(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found"));
		List<Product> products = productRepository.findProductsByCategoryId(categoryId).orElseThrow(
				() -> new ResourceNotFoundException("Products not found of category: " + category.getTitle()));
		return productMapper.toDtoList(products);
	}

	@Override
	public ProductDTO addCategoryToProduct(UUID productId, Long categoryId) {
		Category category = findCategory(categoryId);
		Product product = findProduct(productId);
		
		return null;
	}

	private Product findProduct(UUID productId) {

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found!!!"));
		return product;

	}

	private Category findCategory(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found!!!"));
		return category;
	}

}
