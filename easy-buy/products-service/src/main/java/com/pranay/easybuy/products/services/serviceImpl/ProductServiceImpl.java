package com.pranay.easybuy.products.services.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.pranay.easybuy.products.config.ReviewMapper;
import com.pranay.easybuy.products.dto.CategoryDTO;
import com.pranay.easybuy.products.dto.ReviewDTO;
import com.pranay.easybuy.products.exceptions.InvalidRequestException;
import com.pranay.easybuy.products.models.Review;
import com.pranay.easybuy.products.repositories.ReviewRepository;
import com.pranay.easybuy.products.responseBuilder.PagedResponse;
import com.pranay.easybuy.products.services.CategoryService;
import com.pranay.easybuy.products.services.ImageStorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final ProductMapper productMapper;
	private final CategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;
	private final ReviewMapper reviewMapper;
	private final ReviewRepository reviewRepository;
	private final ImageStorageService imageStorageService;
	private final CategoryService categoryService;

	@Override
	public ProductDTO createProduct(ProductDTO productDTO) {
		Product product = productMapper.toEntity(productDTO);
		List<Category> categories = resolveCategories(productDTO.getCategories());
		product.setCategories(categories);
		Product savedProduct = productRepository.save(product);
		syncCategories(savedProduct, categories);
		return productMapper.toDto(savedProduct);
	}

	@Override
	public PagedResponse<ProductDTO> getAllProducts(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Product> productPage = productRepository.findAll(pageable);
		if (productPage.isEmpty()) {
			throw new InvalidRequestException("No products present!!!");
		}
		return productMapper.toPagedResponseDto(productPage);
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
		product.setShort_desc(productDTO.getShort_desc());
		product.setLong_desc(productDTO.getLong_desc());
		product.setPrice(productDTO.getPrice());
		product.setDiscount(productDTO.getDiscount());
		product.setProductImages(productDTO.getProductImages());
		product.setLive(productDTO.getLive());
		productRepository.save(product);
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
		if (!product.getCategories().contains(category)) {
			product.getCategories().add(category);
		}
		if (!category.getProducts().contains(product)) {
			category.getProducts().add(product);
		}
		categoryRepository.save(category);
		productRepository.save(product);
		return productMapper.toDto(product);
	}

	@Override
	public ProductDTO removeCategoryFromProduct(UUID productId, Long categoryId) {
		Category category = findCategory(categoryId);
		Product product = findProduct(productId);
		product.getCategories().remove(category);
		category.getProducts().remove(product);
		categoryRepository.save(category);
		productRepository.save(product);
		return productMapper.toDto(product);
	}

	@Override
	public ReviewDTO addReviewToProduct(UUID productId, ReviewDTO reviewDTO) {
		Product product = findProduct(productId);
		Review review = reviewMapper.toEntity(reviewDTO);
		review.setProduct(product);
		product.getReviews().add(review);
		reviewRepository.save(review);
		productRepository.save(product);
		return reviewMapper.toDto(review);
	}

	@Override
	public ProductDTO uploadProductImages(UUID productId, List<MultipartFile> files) {
		Product product = findProduct(productId);
		List<String> uploadUrls = uploadImages(files);
		if (product.getProductImages() == null) {
			product.setProductImages(new ArrayList<>());
		}
		product.getProductImages().addAll(uploadUrls);
		return productMapper.toDto(product);
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

	private List<String> uploadImages(List<MultipartFile> files) {
		if (files == null || files.isEmpty()) {
			throw new InvalidRequestException("At least one product image is required");
		}
		List<String> uploadedUrls = new ArrayList<>();
		for (MultipartFile file : files) {
			uploadedUrls.add(imageStorageService.uploadImage(file));
		}
		return uploadedUrls;
	}

	private List<Category> resolveCategories(List<CategoryDTO> categoryDTOS) {
		if (categoryDTOS == null) {
			return new ArrayList<>();
		}
		List<Category> categories = new ArrayList<>();
		for (CategoryDTO categoryDTO : categoryDTOS) {
			if (categoryDTO.getId() == null) {
				Category category = new Category();
				category.setTitle(categoryDTO.getTitle());
				categories.add(categoryRepository.save(category));
			} else {
				categories.add(findCategory(categoryDTO.getId()));
			}
		}
		return categories;
	}

	private void syncCategories(Product product, List<Category> categories) {
		for (Category category : categories) {
			if (!category.getProducts().contains(product)) {
				category.getProducts().add(product);
			}
			categoryRepository.save(category);
		}
	}
}
