package com.whiterabbit.batchutils;

import com.whiterabbit.dto.InputDataLot;
import com.whiterabbit.dto.InputData;
import com.whiterabbit.entities.Article;
import com.whiterabbit.entities.ArticleLot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class ArticleItemProcessor implements ItemProcessor<InputDataLot, ArticleLot> {

    Logger log = LoggerFactory.getLogger(ArticleItemProcessor.class);

    @Override
    public ArticleLot process(InputDataLot inputDataLot) throws Exception {
        ArticleLot articleLot = new ArticleLot();
        for (InputData inputData: inputDataLot.getInputDatas()) {
            Article article = new Article();
            article.setCategory(inputData.getCategory());
            article.setSubcategory(inputData.getSubcategory());
            article.setTitre(inputData.getTitre());
            article.setImg(inputData.getImg());
            article.setUrl(inputData.getUrl());
            article.setAuteur(inputData.getAuteur());
            article.setResume(inputData.getResume());
            log.info("----->ItemProcessor : create article" + article.getTitre());

            articleLot.getArticles().add(article);
        }

        return articleLot;
    }
}
