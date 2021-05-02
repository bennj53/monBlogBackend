package com.whiterabbit.controller;

import com.whiterabbit.dao.ArticleRepository;
import com.whiterabbit.entities.Article;
import com.whiterabbit.exception.ArticleNotFoundException;
import com.whiterabbit.exception.ImpossibleAjouterArticleException;
import com.whiterabbit.services.ArticleService;
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
    ArticleService articleService;

    @GetMapping(value = "/articles")
    public List<Article> listArticles(){
        return articleService.findAll();
    }

    @GetMapping(value = "/articles/category/{category}")
    public List<Article> listArticlesByCategory(@PathVariable String category){
        log.error("passage dans rest listArticlesByCategory : " + category) ;
        List<Article> articles = articleService.findByCategory(category);
        return articles;
    }

    @GetMapping(value = "/articles/{category}/{subcategory}")
    public List<Article> listArticlesBySubcategory(@PathVariable("category") String category, @PathVariable("subcategory") String subcategory){
        log.error("passage dans rest listArticlesBySubcategory : " + category + " " + subcategory) ;
        List<Article> articles = articleService.findBySubcategory(category, subcategory);
        return articles;
    }

    @GetMapping(path="/articles/{id}")
    public Optional<Article> getArticle(@PathVariable String id){
        Optional<Article> article = articleService.getArticleById(id);
        if(!article.isPresent()){
            throw new ArticleNotFoundException("Article not found");
        }
        return article;
    }

    @DeleteMapping(path="/articles/delete/{id}")
    public void deleteArticle(@PathVariable String id){
        Optional<Article> article = articleService.deleteArticleById(id);
        if(!article.isPresent()){
            throw new ArticleNotFoundException("Article not found");
        }
        log.error("article id to delete is  : " + article.get().getId());

    }

    @PostMapping(value = "/articles/add")
    public ResponseEntity<Article> ajouterArticle(@RequestBody Article article){
        log.error("controller save article started");
        Article newArticle = articleService.saveArticle(article);
        if(newArticle == null) throw new ImpossibleAjouterArticleException("Impossible d'ajouter article");

        return new ResponseEntity<Article>(article, HttpStatus.CREATED);
    }

    @PutMapping(value = "/articles/update/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable(value="id") String id,@RequestBody Article article){
        log.error("controller update article started");
        Article articleUpdated = articleService.updateArticleById(id, article);
        if(articleUpdated == null) throw new ImpossibleAjouterArticleException("Impossible d'updater article");

        return new ResponseEntity<Article>(article, HttpStatus.ACCEPTED);
    }
}
