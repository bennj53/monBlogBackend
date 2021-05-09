package com.whiterabbit.utils;

import com.gargoylesoftware.htmlunit.html.*;
import com.whiterabbit.dto.InputData;
import com.whiterabbit.dto.InputDataLot;
import com.whiterabbit.entities.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HtmlReaderDeveloppez implements HtmlReader{
    Logger log = LoggerFactory.getLogger(HtmlReaderDeveloppez.class);
    public static final String DEVELOPPEZ = "developpez.com";
    public static final String category = Category.DEVELOPPEMENT.toString();
    public static final String subcategory = Category.DEVELOPPEMENT.getSubcategory()[0];

    @Override
    public InputDataLot readHtmlPage(String url) {
        HtmlPage page = this.getHtmlPage(url);
        InputDataLot inputDataLot = new InputDataLot();
        if(page != null) {
            List<HtmlDivision> divisions = page.getByXPath("//div[@class='colonneActu']");
            divisions.addAll(page.getByXPath("//div[@class='colonnePubli']"));

            if (divisions.isEmpty()) {
                log.error("Developpez - No division found ! ");
            } else {
                int cpt = 0;
                String articleCategory, articleCategoryUrl, articleTitle, articleUrl, pathImg, articleDate = null;
                List<HtmlArticle> articles = divisions.stream()
                        .map(d->d.getByXPath(".//article"))
                        .flatMap(a->a.stream())
                        .map(a-> (HtmlArticle) a)
                        .collect(Collectors.toList());

                for (HtmlArticle article : articles) {
                    //get article category and category link
                    /*List<HtmlAnchor> anchorCategory = article.getByXPath(".//a[@class='titre']");
                    articleCategory = anchorCategory != null && anchorCategory.size() >= 1 ? anchorCategory.get(0).getTextContent() : null;
                    articleCategoryUrl = anchorCategory != null && anchorCategory.size() >= 1 ? anchorCategory.get(0).getHrefAttribute() : null;
                  *///get article title and link
                    List<HtmlAnchor> anchorArticle = article.getByXPath(".//a[@class='titre']");
                    articleTitle = anchorArticle != null && anchorArticle.size() >= 1 ? anchorArticle.get(0).getTextContent() : null;
                    articleUrl = anchorArticle != null && anchorArticle.size() >= 1 ? anchorArticle.get(0).getHrefAttribute() : null;

                    if(!articleUrl.contains("http") && !articleUrl.contains("www")){
                        articleUrl = url + articleUrl;
                    }

                    //get article img
                    List<HtmlImage> img = article.getByXPath(".//img");
                    pathImg = img != null && img.size() >= 1 ? img.get(0).getAttribute("src") : null;

                    //get article date
                    //List<HtmlParagraph> paragraphsDateArticle = article.getByXPath(".//p[@class='post-card__date has-text-right-mobile']");
                    //articleDate = paragraphsDateArticle != null && paragraphsDateArticle.size() >= 1 ? paragraphsDateArticle.get(0).getTextContent() : null;

                    //debug data
                    //log.info(String.format("Developpez - Article %s --> Category : %s // Url Category : %s", ++cpt, articleCategory, articleCategoryUrl));
                    log.info(String.format("Developpez - Article %s --> Titre : %s // Url : %s", cpt, articleTitle, articleUrl));
                    log.info(String.format("Developpez - Article %s --> Date : %s", cpt, articleDate));
                    log.info(String.format("Developpez - Article %s --> Img : %s", cpt, pathImg));


                    //create article
                    InputData inputData = new InputData();
                    inputData.setCategory(category);
                    inputData.setSubcategory(subcategory);
                    inputData.setTitre(articleTitle);
                    inputData.setImg(pathImg);
                    inputData.setUrl(articleUrl);
                    inputData.setDatePublication(articleDate);
                    inputData.setAuteur(DEVELOPPEZ);
                    //add resume article
                    try {
                        String resume = this.readHtmlPageResume(articleUrl);
                        inputData.setResume(resume);
                        log.info(String.format("Developpez - Article %s --> Resume : %s%n", cpt, inputData.getResume()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    inputDataLot.getInputDatas().add(inputData);
                }
            }
        }else{
            log.error(DEVELOPPEZ + " - No web page found for url : " + url);
        }
        return inputDataLot;
    }

    @Override
    public String readHtmlPageResume(String url) throws IOException {
        String resume = "";
        HtmlPage page = this.getHtmlPage(url);

        if(page != null) {
            List<HtmlDivision> divisions = page.getByXPath("//div[@class='content']");
            resume = divisions != null && divisions.size() >0 ? divisions.get(0).getTextContent():"";

            if (resume.length() > 320){
                resume = resume.substring(0,320);
            }

            if(resume.equals("")){
                List<HtmlSection> sections = page.getByXPath("//section[@class='SectionSynopsis']");
                List<HtmlParagraph> p = sections != null && sections.size() > 0 ? sections.get(0).getByXPath(".//p"):new ArrayList<>();
                int i = 0;
                while(resume.length()<320 && i<p.size()){
                    resume = resume + p.get(i).getTextContent();
                    i++;
                }
            }

        }else{
            log.error(DEVELOPPEZ + " - No resume web page found for url : " + url);
        }

        return resume;
    }
}
