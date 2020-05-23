package com.whiterabbit.entities;

import java.util.ArrayList;
import java.util.List;

public class ArticleLot {
    private List<Article> articles;
    private String source;

    public ArticleLot() {
        this.articles = new ArrayList<>();
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
