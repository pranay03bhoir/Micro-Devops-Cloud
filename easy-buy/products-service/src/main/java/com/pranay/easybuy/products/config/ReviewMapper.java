package com.pranay.easybuy.products.config;

import com.pranay.easybuy.products.dto.ReviewDTO;
import com.pranay.easybuy.products.models.Review;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

//    To DTO
    ReviewDTO toDto(Review review);

//    To Entity
    Review toEntity(ReviewDTO reviewDTO);

//    Review List to DTO
    List<ReviewDTO> toDtoList(List<Review> reviews);
}
