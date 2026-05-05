package com.pranay.easybuy.products.repositories;

import com.pranay.easybuy.products.models.Products;
import com.pranay.easybuy.products.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<List<Review>> findReviewsByProduct(Products product);
}
