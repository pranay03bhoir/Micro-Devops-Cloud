package com.pranay.easybuy.products.repositories;

import com.pranay.easybuy.products.models.Product;
import com.pranay.easybuy.products.models.Review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.id = :categoryId")
    Optional<List<Product>> findProductsByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.id = :categoryId")
    Page<Product> findProductsByCategoryId(Long categoryId, Pageable pageable);
}
