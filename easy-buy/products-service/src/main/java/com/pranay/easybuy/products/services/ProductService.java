package com.pranay.easybuy.products.services;

import com.pranay.easybuy.products.dto.ProductDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO);

    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(UUID productId);

    ProductDTO updateProduct(ProductDTO productDTO, UUID productId);

    String deleteProduct(UUID productId);

    List<ProductDTO> getProductsByCategory(Long categoryId);

}
