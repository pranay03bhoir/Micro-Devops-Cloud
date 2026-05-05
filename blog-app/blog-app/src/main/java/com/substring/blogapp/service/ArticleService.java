package com.substring.blogapp.service;

import com.substring.blogapp.dto.ArticleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArticleService {

    List<ArticleDto> getAll();

    Page<ArticleDto> getAllArticlesPaginated(Pageable pageable);

    ArticleDto getArticleById(Long articleId);

    ArticleDto createArticle(ArticleDto articleDto);

    ArticleDto updateArticle(ArticleDto articleDto, Long id);

    void deleteArticle(Long articleId);

    List<ArticleDto> getArticleOfCategory(Long categoryId);

    List<ArticleDto> getArticleOfUser(Long userId);
}
