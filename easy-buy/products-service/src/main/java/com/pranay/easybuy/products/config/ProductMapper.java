package com.pranay.easybuy.products.config;

import com.pranay.easybuy.products.dto.ProductDTO;
import com.pranay.easybuy.products.models.Product;
import com.pranay.easybuy.products.responseBuilder.PagedResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

//    To DTO
	@Mapping(target = "short_desc", source = "short_desc")
	@Mapping(target = "long_desc", source = "long_desc")
	@Mapping(target = "createdAt", source = "createdAt")
	@Mapping(target = "updatedAt", source = "updatedAt")
	ProductDTO toDto(Product product);

//    To Entity
	@Mapping(target = "short_desc", source = "short_desc")
	@Mapping(target = "long_desc", source = "long_desc")
	@Mapping(target = "createdAt", source = "createdAt")
	@Mapping(target = "updatedAt", source = "updatedAt")
	Product toEntity(ProductDTO productDTO);

//    Product List to DTO
	List<ProductDTO> toDtoList(List<Product> products);

//    To PagedResponse ProductDto
	@Mapping(target = "content", source = "content")
	@Mapping(target = "pageNumber", source = "number")
	@Mapping(target = "pageSize", source = "size")
	@Mapping(target = "totalElements", source = "totalElements")
	@Mapping(target = "totalPages", source = "totalPages")
	@Mapping(target = "numberOfElements", source = "numberOfElements")
	@Mapping(target = "first", source = "first")
	@Mapping(target = "last", source = "last")
	PagedResponse<ProductDTO> toPagedResponseDto(Page<Product> product);
}
