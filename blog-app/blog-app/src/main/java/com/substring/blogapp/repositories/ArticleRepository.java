package com.substring.blogapp.repositories;

import com.substring.blogapp.models.Article;
import com.substring.blogapp.models.Category;
import com.substring.blogapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    // Custom JPA methods.
    List<Article> findByCategory(Category category);

    List<Article> findByUser(User user);

    List<Article> findByCategoryAndUser(Category category, User user);
}
