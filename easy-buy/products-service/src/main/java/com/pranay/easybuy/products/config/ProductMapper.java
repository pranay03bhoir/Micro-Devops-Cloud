package com.pranay.easybuy.products.config;

import com.pranay.easybuy.products.dto.ProductDTO;
import com.pranay.easybuy.products.models.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

//    To DTO
    ProductDTO toDto(Product product);

//    To Entity
    Product toEntity(ProductDTO productDTO);

//    Product List to DTO
    List<ProductDTO> toDtoList(List<Product> products);
}
