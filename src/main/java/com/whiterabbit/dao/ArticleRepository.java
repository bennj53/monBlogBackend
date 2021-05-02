package com.whiterabbit.dao;

import com.whiterabbit.entities.Article;
import com.whiterabbit.entities.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ArticleRepository extends MongoRepository<Article, String> {

    List<Article> findByAuteurNot(String auteur);
    List<Article> findByCategory(String category);
    List<Article> findBySubcategory(String subcategory);
}
