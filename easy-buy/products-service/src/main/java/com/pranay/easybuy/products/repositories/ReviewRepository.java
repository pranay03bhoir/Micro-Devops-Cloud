package com.pranay.easybuy.products.repositories;

import com.pranay.easybuy.products.models.Product;
import com.pranay.easybuy.products.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<List<Review>> findReviewsByProduct(Product product);

    @Query("SELECT r FROM Review r WHERE r.product.id = :productId")
    Optional<List<Review>> findReviewsByProductId(UUID productId);
}
