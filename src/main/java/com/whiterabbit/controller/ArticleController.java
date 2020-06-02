package com.whiterabbit.controller;

import com.whiterabbit.dao.ArticleRepository;
import com.whiterabbit.entities.Article;
import com.whiterabbit.exception.ArticleNotFoundException;
import com.whiterabbit.exception.ImpossibleAjouterArticleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
public class ArticleController {

    Logger log = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    ArticleRepository articleRepository;

    @GetMapping(value = "/articles")
    public List<Article> listeArticles(){
        List<Article> articles = articleRepository.findAll();
        return articles;
    }

    @GetMapping(path="/articles/{id}")
    public Optional<Article> getArticle(@PathVariable String id){
        Optional<Article> article = articleRepository.findById(id);

        if(!article.isPresent()){
            throw new ArticleNotFoundException("Article not found");
        }

        return article;
    }

    @PostMapping(value = "/articles/add")
    public ResponseEntity<Article> ajouterArticle(@RequestBody Article article){
        log.info("controller save article started");
        Article newArticle = articleRepository.save(article);
        if(newArticle == null) throw new ImpossibleAjouterArticleException("Impossible d'ajouter article");

        return new ResponseEntity<Article>(article, HttpStatus.CREATED);
    }
}
