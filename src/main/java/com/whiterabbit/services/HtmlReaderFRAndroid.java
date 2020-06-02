package com.whiterabbit.services;

import com.gargoylesoftware.htmlunit.html.*;
import com.whiterabbit.dto.InputData;
import com.whiterabbit.dto.InputDataLot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Class to read and extract data from https://www.frandroid.com/actualites
 */
public class HtmlReaderFRAndroid implements HtmlReader {

    Logger log = LoggerFactory.getLogger(HtmlReaderFRAndroid.class);
    public static final String FRANDROID = "frandroid.com";

    @Override
    public InputDataLot readHtmlPage(String url) {
            HtmlPage page = this.getHtmlPage(url);
            InputDataLot inputDataLot = new InputDataLot();

            List<HtmlDivision> divisions = page.getByXPath("//div[@class='columns is-vcentered is-mobile is-relative']");
            if(divisions.isEmpty()){
                log.error("frandroid - No division found ! ");
            }else {
                int cpt = 0;
                String articleCategory, articleCategoryUrl, articleTitle, articleUrl, pathImg, articleDate = null;

                for (HtmlDivision division : divisions) {
                    //get article category and category link
                    List<HtmlAnchor> anchorCategory = division.getByXPath(".//a[@class='post-card__category is-uppercase has-text-weight-semibold']");
                    articleCategory = anchorCategory != null && anchorCategory.size()>=1 ? anchorCategory.get(0).getTextContent():null;
                    articleCategoryUrl = anchorCategory != null && anchorCategory.size()>=1 ? anchorCategory.get(0).getHrefAttribute():null;
                    //get article title and link
                    List<HtmlAnchor> anchorArticle = division.getByXPath(".//a[@class='post-card__title-link']");
                    articleTitle = anchorArticle != null && anchorArticle.size()>=1 ? anchorArticle.get(0).getTextContent():null;
                    articleUrl = anchorArticle != null && anchorArticle.size()>=1 ? anchorArticle.get(0).getHrefAttribute():null;
                    //get article img
                    List<HtmlImage> img = division.getByXPath(".//img");
                    pathImg = img != null && img.size()>=1 ? img.get(0).getAttribute("data-srcset") : null;
                    if(pathImg != null && pathImg.split(",").length>1){
                        pathImg = pathImg.split(",")[1];
                    }
                    //get article date
                    List<HtmlParagraph> paragraphsDateArticle = division.getByXPath(".//p[@class='post-card__date has-text-right-mobile']");
                    articleDate = paragraphsDateArticle != null && paragraphsDateArticle.size()>=1 ? paragraphsDateArticle.get(0).getTextContent() : null;

                    //debug data
                    log.info(String.format("frandroid - Article %s --> Category : %s // Url Category : %s", ++cpt, articleCategory, articleCategoryUrl));
                    log.info(String.format("frandroid - Article %s --> Titre : %s // Url : %s", cpt, articleTitle, articleUrl));
                    log.info(String.format("frandroid - Article %s --> Date : %s",  cpt,articleDate));
                    log.info(String.format("frandroid - Article %s --> Img : %s",  cpt,pathImg));


                    //create article
                    InputData inputData = new InputData();
                    inputData.setTitre(articleTitle);
                    inputData.setImg(pathImg);
                    inputData.setUrl(articleUrl);
                    inputData.setDatePublication(articleDate);
                    inputData.setAuteur(FRANDROID);
                    //add resume article
                    try {
                        String resume = this.readHtmlPageResume(articleUrl);
                        inputData.setResume(resume);
                        log.info(String.format("frandroid - Article %s --> Resume : %s%n",  cpt, inputData.getResume()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    inputDataLot.getInputDatas().add(inputData);
                }
            }
            return inputDataLot;
    }

    @Override
    public String readHtmlPageResume(String url) throws IOException {
        HtmlPage page = this.getHtmlPage(url);
        String resume= null;

        List<HtmlParagraph> paragraphArticleResume = page.getByXPath(".//p[@class='chapo']");
        String articleResume = paragraphArticleResume != null && paragraphArticleResume.size()>=1 ? paragraphArticleResume.get(0).getTextContent() : null;

        if(articleResume == null){
            log.error("frandroid - No article resume found ! ");
        }

        return articleResume;
    }
}
