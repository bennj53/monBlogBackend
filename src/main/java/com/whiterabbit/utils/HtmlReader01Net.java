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
 * Class to read and extract data from https://www.01net.com/actualites/
 */
public class HtmlReader01Net implements HtmlReader {

    Logger log = LoggerFactory.getLogger(HtmlReader01Net.class);
    public static final String ZERO1NET = "01net.com";
    public static final String category = Category.GENERAL.toString();
    public static final String subcategory = Category.GENERAL.getSubcategory()[0];

    @Override
    public InputDataLot readHtmlPage(String url) {

            HtmlPage page = this.getHtmlPage(url);
            InputDataLot inputDataLot = new InputDataLot();

            if(page != null) {
                List<HtmlArticle> articles = page.getByXPath("//article[@class='timeline-panel bg-color-1 row no-margin']");
                if (articles.isEmpty()) {
                    log.error("01Net - No article found ! ");
                } else {
                    int cpt = 0;
                    String articleTextContent = null;
                    String articleUrl = null;
                    String pathImg = null;
                    log.info("01Net - article found ! ");

                    for (HtmlArticle article : articles) {
                        List<HtmlHeading2> itemH2s = article.getByXPath(".//h2");
                        articleTextContent = itemH2s != null && itemH2s.size() >= 1 ? itemH2s.get(0).getTextContent() : null;
                        List<HtmlAnchor> itemAnchors = article.getByXPath(".//a");
                        articleUrl = itemAnchors != null && itemAnchors.size() >= 1 ? itemAnchors.get(0).getHrefAttribute() : null;
                        if (!articleUrl.contains("http"))
                            articleUrl = "https:" + articleUrl;
                        List<HtmlImage> itemImgs = article.getByXPath(".//img");
                        pathImg = itemImgs != null && itemImgs.size() >= 1 ? itemImgs.get(0).getAttribute("data-original") : null;
                        log.info(String.format("01Net - Article %s --> Titre : %s // Url : %s", ++cpt, articleTextContent, articleUrl));
                        log.info(String.format("01Net - Img : %s", pathImg));

                        //créer l' Article
                        InputData inputData = new InputData();
                        inputData.setCategory(category);
                        inputData.setSubcategory(subcategory);
                        inputData.setTitre(articleTextContent);
                        inputData.setImg(pathImg);
                        inputData.setUrl(articleUrl);
                        inputData.setAuteur(ZERO1NET);
                        //add resume article
                        try {
                            String resume = readHtmlPageResume(articleUrl);
                            inputData.setResume(resume);
                            log.info(String.format("01Net - Resume : %s%n", inputData.getResume()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        inputDataLot.getInputDatas().add(inputData);
                    }
                }
            }else{
                log.error(ZERO1NET + " - No web page found for url : " + url);
            }
        return inputDataLot;
    }

    @Override
    public String readHtmlPageResume(String url) throws IOException {
        HtmlPage page = this.getHtmlPage(url);
        String resume = null;

        if(page != null){
            List<HtmlHeading2> heading2s = page.getByXPath("//h2[@class='title-large padding-bottomx2 blocx3 border-b-s']");
            if(heading2s == null || heading2s.isEmpty()){
                log.error("01Net - No h2 found ! ");
            }else{
                for(HtmlElement heading2 : heading2s) {
                    resume = heading2.getTextContent();

                }
            }
        }else{
            log.error(url + " - page resume not found");
        }
        return resume;
    }
}
