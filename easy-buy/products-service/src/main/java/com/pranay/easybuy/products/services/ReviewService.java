package com.pranay.easybuy.products.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pranay.easybuy.products.dto.ReviewDTO;

@Service
public interface ReviewService {
    List<ReviewDTO> getAllReviews();

    ReviewDTO getReviewById(Long reviewId);

    List<ReviewDTO> getReviewsByProductId(UUID productId);

    ReviewDTO createReview(UUID productId, ReviewDTO reviewDto);

    ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDto);

    ReviewDTO deleteReview(Long reviewId);
}
