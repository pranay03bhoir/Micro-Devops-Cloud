package com.pranay.easybuy.products.repositories;

import com.pranay.easybuy.products.models.Category;
import com.pranay.easybuy.products.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c JOIN c.products p WHERE p.id = :productId")
    Optional<List<Category>> findCategoriesByProductId(@Param("productId") UUID productId);
}
