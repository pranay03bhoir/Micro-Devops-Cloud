package com.substring.blogapp.service.serviceImpl;

import com.substring.blogapp.dto.ArticleDto;
import com.substring.blogapp.exceptions.ResourceNotFoundException;
import com.substring.blogapp.models.Article;
import com.substring.blogapp.repositories.ArticleRepository;
import com.substring.blogapp.repositories.CategoryRepository;
import com.substring.blogapp.repositories.UserRepository;
import com.substring.blogapp.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


//@Service("ArticleServiceDB")
@Service
@RequiredArgsConstructor
@Primary
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public List<ArticleDto> getAll() {
        List<Article> articles = articleRepository.findAll();
        return articles.stream()
                .map((article -> modelMapper.map(article, ArticleDto.class))).toList();
    }

    @Override
    public ArticleDto getArticleById(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ResourceNotFoundException("Article not found!!!"));
        return modelMapper.map(article, ArticleDto.class);
    }

    @Override
    public ArticleDto createArticle(ArticleDto articleDto) {
        Article article = modelMapper.map(articleDto, Article.class);
        article.setCreatedAt(LocalDateTime.now());
        if (articleDto.getCategoryId() != null) {
            var category = categoryRepository.findById(articleDto.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            article.setCategory(category);
        }
        Article savedArticle = articleRepository.save(article);
        return modelMapper.map(savedArticle, ArticleDto.class);
    }

    @Override
    public ArticleDto updateArticle(ArticleDto articleDto, Long id) {
        Article existingArticle = articleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        existingArticle.setTitle(articleDto.getTitle());
        existingArticle.setShortDesc(articleDto.getShortDesc());
        existingArticle.setContent(articleDto.getContent());
        existingArticle.setPaid(articleDto.getPaid());
        existingArticle.setStatus(articleDto.getStatus());
        existingArticle.setReadingMinutes(articleDto.getReadingMinutes());
        existingArticle.setRating(articleDto.getRating());
        existingArticle.setPrice(articleDto.getPrice());
        existingArticle.setPublishedAt(articleDto.getPublishedAt());

        // conditionally assigning category to the article
        if (articleDto.getCategoryId() != null) {
            var category = categoryRepository.findById(articleDto.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            existingArticle.setCategory(category);
        }

        Article updatedArticle = modelMapper.map(existingArticle, Article.class);
        Article savedArticle = articleRepository.save(updatedArticle);
        return modelMapper.map(savedArticle, ArticleDto.class);
    }

    @Override
    public void deleteArticle(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        articleRepository.delete(article);

    }

    @Override
    public List<ArticleDto> getArticleOfCategory(Long categoryId) {
        var category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found!!!"));
        List<Article> articles = articleRepository.findByCategory(category);
        return articles.stream().map((article -> modelMapper.map(article, ArticleDto.class))).toList();
    }

    @Override
    public List<ArticleDto> getArticleOfUser(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!!!"));
        return articleRepository.findByUser(user).stream().map((article -> modelMapper.map(article, ArticleDto.class))).toList();
    }
}
