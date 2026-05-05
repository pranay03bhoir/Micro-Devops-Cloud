package com.pranay.easybuy.products.repositories;

import com.pranay.easybuy.products.models.Category;
import com.pranay.easybuy.products.models.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<List<Category>> findCategoriesByProducts(List<Products> products);
}
