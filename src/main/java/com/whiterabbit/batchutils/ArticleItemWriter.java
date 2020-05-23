package com.whiterabbit.batchutils;

import com.whiterabbit.dao.ArticleRepository;
import com.whiterabbit.entities.ArticleLot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArticleItemWriter implements ItemWriter<ArticleLot> {
    Logger log = LoggerFactory.getLogger(ArticleItemWriter.class);

    @Autowired
    ArticleRepository articleRepository;

    @Override
    public void write(List<? extends ArticleLot> list) throws Exception {
        log.error("----->ItemWriter : nb sources articles : " + list.size());
        for (ArticleLot articleLot : list) {
            log.error("----->ItemWriter : nb saved articles for " + articleLot.getArticles().get(0).getAuteur() +" : " + articleLot.getArticles().size());
            articleRepository.saveAll(articleLot.getArticles());
        }
    }
}
