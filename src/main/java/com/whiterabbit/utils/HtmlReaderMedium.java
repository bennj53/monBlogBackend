package com.whiterabbit.utils;

import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.whiterabbit.dto.InputData;
import com.whiterabbit.dto.InputDataLot;
import com.whiterabbit.entities.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class HtmlReaderMedium implements HtmlReader{
    Logger log = LoggerFactory.getLogger(HtmlReaderMedium.class);
    public static final String MEDIUM = "medium.com";
    public static final String category = Category.DEVELOPPEMENT.toString();
    public static final String subcategory = Category.DEVELOPPEMENT.getSubcategory()[1];

    @Override
    public InputDataLot readHtmlPage(String url) {

        HtmlPage page = this.getHtmlPage(url);
        InputDataLot inputDataLot = new InputDataLot();

        if(page != null) {
            List<HtmlSection> sections = page.getByXPath("//section[@class='ha hb n']");

            if (sections.isEmpty()) {
                log.error(MEDIUM+" - No article found ! ");
            } else {
                log.error(MEDIUM+" - articles found ! ");
                int cpt = 0;
                String articleTextContent = null;
                String resume = null;
                String articleUrl = null;
                String logo = null;
                //bh fx gd bj aw em en at eo av ep gi aq

                for (HtmlSection article : sections) {
                    List<HtmlHeading3> itemH3s = article.getByXPath(".//h3");
                    if(itemH3s != null && itemH3s.size() >= 1){
                       articleTextContent =  itemH3s.get(0).getTextContent();

                        resume =  itemH3s.size()>1 ? itemH3s.get(1).getTextContent() : null;

                       List<HtmlAnchor> itemAnchors = itemH3s.get(0).getByXPath(".//a");
                       articleUrl = itemAnchors != null && itemAnchors.size() >= 1 ? itemAnchors.get(0).getHrefAttribute() : null;
                    }

                    if (!articleUrl.contains("http"))
                        articleUrl = "https:" + articleUrl;

                    List<HtmlDivision> divisions = article.getByXPath("//div[@class='fl s g']");
/*                    if(divisions != null && divisions.size()>0){
                        List<HtmlAnchor> anchors = divisions.get(0).getByXPath(".//a");
                        logo = anchors != null && anchors.size() >= 1 ? anchors.get(0).getHrefAttribute() : null;

                    }*/


                    log.info(String.format(MEDIUM+" - Article %s --> Titre : %s // Url : %s", ++cpt, articleTextContent, articleUrl));
                    log.info(String.format(MEDIUM+" - Img : %s", logo));
                    log.info(String.format(MEDIUM+" - Resume : %s", resume));

                    //créer l' Article
                    InputData inputData = new InputData();
                    inputData.setCategory(category);
                    inputData.setSubcategory(subcategory);
                    inputData.setTitre(articleTextContent);
                    inputData.setImg(logo);
                    inputData.setUrl(articleUrl);
                    inputData.setAuteur(MEDIUM);
                    inputData.setResume(resume);

                    inputDataLot.getInputDatas().add(inputData);
                }
            }
        }else{
            log.error(MEDIUM + " - No web page found for url : " + url);
        }
        return inputDataLot;


    }

    @Override
    public String readHtmlPageResume(String url) throws IOException {
        return null;
    }
}
