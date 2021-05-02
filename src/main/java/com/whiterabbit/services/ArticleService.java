package com.whiterabbit.services;

import com.whiterabbit.entities.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleService {
    Article saveArticle(Article article);
    Article updateArticleById(String id, Article article);
    Optional<Article> deleteArticleById(String id);
    Optional<Article> getArticleById(String id);
    List<Article> findAll();
    List<Article> findByCategory(String category);
    List<Article> findBySubcategory(String category, String subcategory);
}
