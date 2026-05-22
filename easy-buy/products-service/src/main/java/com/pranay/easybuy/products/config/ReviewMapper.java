package com.pranay.easybuy.products.config;

import com.pranay.easybuy.products.dto.ReviewDTO;
import com.pranay.easybuy.products.models.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

//    To DTO
	@Mapping(target = "createdAt", source = "createdAt")
	@Mapping(target = "updatedAt", source = "updatedAt")
	ReviewDTO toDto(Review review);

//    To Entity
	@Mapping(target = "createdAt", source = "createdAt")
	@Mapping(target = "updatedAt", source = "updatedAt")
	Review toEntity(ReviewDTO reviewDTO);

//    Review List to DTO
	List<ReviewDTO> toDtoList(List<Review> reviews);
}
