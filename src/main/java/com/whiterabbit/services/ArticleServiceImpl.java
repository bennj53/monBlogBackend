package com.whiterabbit.services;

import com.whiterabbit.dao.ArticleRepository;
import com.whiterabbit.entities.Article;
import com.whiterabbit.entities.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService{

    Logger logger = LoggerFactory.getLogger(ArticleService.class);

    @Autowired
    ArticleRepository articleRepository;

    @Override
    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    @Override
    public Article updateArticleById(String id, Article article) {
        article.setId(id);
        return articleRepository.save(article);
    }

    @Override
    public Optional<Article> deleteArticleById(String id) {
        return articleRepository.findById(id);
    }

    @Override
    public Optional<Article> getArticleById(String id) {
        return articleRepository.findById(id);
    }

    @Override
    public List<Article> findAll() {
        return  articleRepository.findAll();
    }

    @Override
    public List<Article> findByCategory(String category) {
        logger.error("passage dans service article findByCategory, request parameters, category is " + category);
        List<Article> articles = new ArrayList<>();

        try{
            Category categoryEnum= Category.valueOf(category.toUpperCase());
            logger.error("category to string : " + categoryEnum.toString());
            articles.addAll(articleRepository.findByCategory(categoryEnum.toString()));
        }catch(IllegalArgumentException e){
            logger.error("undefined category request parameter");
            logger.error(e.getMessage());
        }finally {
            return articles;
        }
    }

    @Override
    public List<Article> findBySubcategory(String category, String subcategory) {
        logger.error("passage dans service article findBySubcategory, request parameters, category is " + category + " and subcategory is " + subcategory);
        List<Article> articles = new ArrayList<>();

        try{
            Category categoryEnum= Category.valueOf(category.toUpperCase());
            if (!Arrays.asList(categoryEnum.getSubcategory()).contains(subcategory)){
                throw new IllegalArgumentException();
            }
            articles.addAll(articleRepository.findBySubcategory(subcategory));
        }catch(IllegalArgumentException e){
            logger.error("undefined category or sous category request parameter, category is " + category + " and sous category is " + subcategory);
            logger.error(e.getMessage());
        }finally {
            return articles;
        }
    }
}
