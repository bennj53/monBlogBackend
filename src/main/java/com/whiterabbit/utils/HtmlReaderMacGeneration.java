package com.whiterabbit.utils;

import com.gargoylesoftware.htmlunit.html.*;
import com.whiterabbit.dto.InputData;
import com.whiterabbit.dto.InputDataLot;
import com.whiterabbit.entities.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Class to read and extract data from https://www.macg.co/
 */
public class HtmlReaderMacGeneration implements HtmlReader {

    Logger log = LoggerFactory.getLogger(HtmlReaderMacGeneration.class);
    public static final String MACGENERATION = "macg.co";
    public static final String category = Category.GENERAL.toString();
    public static final String subcategory = Category.GENERAL.getSubcategory()[0];

    @Override
    public InputDataLot readHtmlPage(String url) {
            HtmlPage page = this.getHtmlPage(url);
            InputDataLot inputDataLot = new InputDataLot();
            if(page != null) {
                List<HtmlArticle> articlesHtml = page.getByXPath("//article[@class='pas']");
                if(articlesHtml.isEmpty()){
                    log.error("MacGeneration - No articleHtml found ! ");
                }else {
                    String articleUrl = null;
                    String articleImg = null;
                    String articleTitle = null;
                    int cpt = 0;

                    for (HtmlArticle articleHtml : articlesHtml){
                        //get article url
                        List<HtmlAnchor> anchorArticle = articleHtml.getByXPath(".//a");
                        articleUrl = anchorArticle != null && anchorArticle.size()>=1 ? anchorArticle.get(0).getHrefAttribute():null;
                        if (!articleUrl.contains("http"))
                            articleUrl = "https://www.macg.co" + articleUrl;
                        //get article img
                        List<HtmlImage> imgArticle = articleHtml.getByXPath(".//img");
                        articleImg = imgArticle != null && imgArticle.size()>=1 ? imgArticle.get(0).getSrcAttribute() : null;
                        //get article title
                        List<HtmlHeading2> h2Article = articleHtml.getByXPath(".//h2");
                        articleTitle = h2Article != null && h2Article.size()>=1 ? h2Article.get(0).getTextContent().trim() : null;

                        //debug data
                        //System.out.println(String.format("MacGeneration - Article %s --> Category : %s // Url Category : %s", ++cpt, articleCategory, articleCategoryUrl));
                        log.info(String.format("MacGeneration - Article %s --> Titre : %s // Url : %s", ++cpt, articleTitle, articleUrl));
                        //System.out.println(String.format("MacGeneration - Article %s --> Date : %s",  cpt,articleDate));
                        log.info(String.format("MacGeneration - Article %s --> Img : %s",  cpt,articleImg));

                        //create article
                        InputData inputData = new InputData();
                        inputData.setCategory(category);
                        inputData.setSubcategory(subcategory);
                        inputData.setTitre(articleTitle);
                        inputData.setImg(articleImg);
                        inputData.setUrl(articleUrl);
                        //articl.setDatePublication(articleDate);
                        inputData.setAuteur(MACGENERATION);
                        //add resume article
                        try {
                            String resume = this.readHtmlPageResume(articleUrl);
                            inputData.setResume(resume);
                            log.info(String.format("MacGeneration - Article %s --> Resume : %s%n",  cpt, inputData.getResume()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        inputDataLot.getInputDatas().add(inputData);

                        //limite de 20 articles images trop lourdes
                        if (cpt == 20){
                            break;
                        }
                    }
                }
            }else{
                log.error(MACGENERATION + " - No web page found for url : " + url);
            }
            return inputDataLot;
    }

    @Override
    public String readHtmlPageResume(String url) throws IOException {
        HtmlPage page = this.getHtmlPage(url);
        String articleResume= null;

        if(page != null) {
            List<HtmlDivision> divisionArticleResume = page.getByXPath(".//div[@class='field-item even']");
            if(divisionArticleResume != null && divisionArticleResume.size()>0){
                List<HtmlParagraph> paragraphsResume = divisionArticleResume.get(0).getByXPath(".//p");
                articleResume = paragraphsResume != null && paragraphsResume.size()>0 ? paragraphsResume.get(0).getTextContent() : null;
            }else{
                log.error("MacGeneration - No division for article resume found !");
            }
        }

        if(articleResume == null){
            log.error("MacGeneration - No article resume found ! ");
        }

        return articleResume;
    }
}
