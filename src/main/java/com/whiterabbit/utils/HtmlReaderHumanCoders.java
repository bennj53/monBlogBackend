package com.whiterabbit.utils;

import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.html.HtmlMeta;
import com.whiterabbit.dto.InputData;
import com.whiterabbit.dto.InputDataLot;
import com.whiterabbit.entities.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class HtmlReaderHumanCoders implements HtmlReader {

    Logger log = LoggerFactory.getLogger(HtmlReaderHumanCoders.class);
    public static final String HUMANCODERS = "humancoders.com";
    public static final String category = Category.DEVELOPPEMENT.toString();
    public static final String subcategory = Category.DEVELOPPEMENT.getSubcategory()[0];

    @Override
    public InputDataLot readHtmlPage(String url) {
        HtmlPage page = this.getHtmlPage(url);
        InputDataLot inputDataLot = new InputDataLot();

        if(page != null) {
            List<HtmlDivision> divs = page.getByXPath("//div[@class='item-wrapper']");
            if (divs.isEmpty()) {
                log.error("HumanCoders - No article found ! ");
            } else {
                log.error("HumanCoders - articles found ! ");
                int cpt = 0;
                String articleTextContent = null;
                String articleUrl = null;
                List<HtmlMeta> metaTags = page.getByXPath("//meta[@property='og:image']");;
                String logo = metaTags != null && metaTags.size() > 0 ? metaTags.get(0).getContentAttribute():"";//pageMetaProperties.get(0).getContent();
                log.info("HumanCoders - article found ! ");

                for (HtmlDivision article : divs) {
                    List<HtmlHeading3> itemH3s = article.getByXPath(".//h3");
                    articleTextContent = itemH3s != null && itemH3s.size() >= 1 ? itemH3s.get(0).getTextContent() : null;

                    List<HtmlAnchor> itemAnchors = article.getByXPath(".//a");
                    articleUrl = itemAnchors != null && itemAnchors.size() >= 1 ? itemAnchors.get(0).getHrefAttribute() : null;

                    if (!articleUrl.contains("http"))
                        articleUrl = "https:" + articleUrl;

                    log.info(String.format("HumanCoders - Article %s --> Titre : %s // Url : %s", ++cpt, articleTextContent, articleUrl));
                    log.info(String.format("HumanCoders - Img : %s", logo));

                    //créer l' Article
                    InputData inputData = new InputData();
                    inputData.setCategory(category);
                    inputData.setSubcategory(subcategory);
                    inputData.setTitre(articleTextContent);
                    inputData.setImg(logo);
                    inputData.setUrl(articleUrl);
                    inputData.setAuteur(HUMANCODERS);

                    inputDataLot.getInputDatas().add(inputData);
                }
            }
        }else{
            log.error(HUMANCODERS + " - No web page found for url : " + url);
        }
        return inputDataLot;
    }

    @Override
    public String readHtmlPageResume(String url) throws IOException {
        return null;
    }
}
