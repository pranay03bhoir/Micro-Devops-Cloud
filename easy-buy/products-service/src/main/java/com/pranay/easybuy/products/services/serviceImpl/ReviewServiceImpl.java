package com.pranay.easybuy.products.services.serviceImpl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pranay.easybuy.products.config.ReviewMapper;
import com.pranay.easybuy.products.dto.ReviewDTO;
import com.pranay.easybuy.products.exceptions.ResourceNotFoundException;
import com.pranay.easybuy.products.models.Product;
import com.pranay.easybuy.products.models.Review;
import com.pranay.easybuy.products.repositories.ProductRepository;
import com.pranay.easybuy.products.repositories.ReviewRepository;
import com.pranay.easybuy.products.services.ReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ProductRepository productRepository;

    @Override
    public List<ReviewDTO> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviewMapper.toDtoList(reviews);
    }

    @Override
    public ReviewDTO getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found!!!"));
        return reviewMapper.toDto(review);
    }

    @Override
    public List<ReviewDTO> getReviewsByProductId(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!!!"));
        List<Review> reviews = reviewRepository.findReviewsByProductId(product.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Reviews not found!!!"));
        return reviewMapper.toDtoList(reviews);
    }

    @Override
    public ReviewDTO createReview(UUID productId, ReviewDTO reviewDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        Review createdReview = reviewMapper.toEntity(reviewDto);
        Review savedReview = reviewRepository.save(createdReview);
        product.getReviews().add(savedReview);
        productRepository.save(product);
        return reviewMapper.toDto(savedReview);
    }

    @Override
    public ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Reviews not found!!!"));
        review.setTitle(reviewDto.getTitle());
        review.setComment(reviewDto.getComment());
        review.setRating(reviewDto.getRating());
        Review updatedReview = reviewRepository.save(review);
        return reviewMapper.toDto(updatedReview);
    }

    @Override
    public ReviewDTO deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Reviews not found!!!"));
        reviewRepository.deleteById(reviewId);
        return reviewMapper.toDto(review);
    }
}
