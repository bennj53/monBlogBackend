package com.whiterabbit.services;

import com.gargoylesoftware.htmlunit.html.*;
import com.whiterabbit.dto.InputDataLot;
import com.whiterabbit.dto.InputData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Class to read and extract data from https://www.journaldugeek.com/articles/
 */
public class HtmlReaderJDG implements HtmlReader {

    Logger log = LoggerFactory.getLogger(HtmlReaderJDG.class);
    public static final String JDG = "journaldugeek.com";

    @Override
    public InputDataLot readHtmlPage(String url) {
        HtmlPage htmlPage = this.getHtmlPage(url);
        InputDataLot inputDataLot = new InputDataLot();
        List<HtmlElement> items = htmlPage.getByXPath("//section[@class='archive__content page__content']");

        if(items.isEmpty()){
            log.error("JDG - No items found ! ");
        }else{
            for(HtmlElement htmlItem : items){
                List<HtmlAnchor> itemAnchors =  htmlItem.getByXPath(".//a");

                int cpt = 0;

                for (HtmlAnchor anchor : itemAnchors) {
                    String itemUrl = anchor.getHrefAttribute() ;
                    String itemTitle = anchor.getAttribute("title");
                    log.info( String.format("JDG - Title %s : %s --> Url : %s", ++cpt,itemTitle,itemUrl));


                    List<HtmlImage> imgAnchors = anchor.getByXPath(".//img");
                    for(HtmlImage img : imgAnchors){
                        String pathImg = img.getAttribute("data-srcset");
                        pathImg = pathImg.split(",",0)[0].trim();
                        log.info( String.format("JDG - Img Path : %s%n", pathImg));

                        //créer l' Article
                        InputData inputData = new InputData();
                        inputData.setTitre(itemTitle);
                        inputData.setImg(pathImg);
                        inputData.setUrl(itemUrl);
                        inputData.setAuteur(JDG);
                        //add resume article
                        try {
                            String resume = readHtmlPageResume(itemUrl);
                            inputData.setResume(resume);
                            log.info( String.format("JDG - Resume : %s%n", inputData.getResume()));
                            //articleRepository.save(article);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        inputDataLot.getInputDatas().add(inputData);
                    }
                }

            }
        }
        return inputDataLot;
    }

    public String readHtmlPageResume(String url) throws IOException {
        HtmlPage page = this.getHtmlPage(url);
        String firstParagraph = null;

        List<HtmlElement> items = page.getByXPath("//div[@class='single__post']");
        if(items.isEmpty()){
            log.error("JDG - No p found ! ");
        }else{
            for(HtmlElement htmlItem : items) {
                List<HtmlParagraph> itemParagraph = htmlItem.getByXPath(".//p");

                int cpt= 1;

                for (HtmlParagraph paragraph : itemParagraph) {
                    if (cpt == 1){
                        firstParagraph = paragraph.getTextContent();
                        if(firstParagraph != null && !firstParagraph.equals("")) {
                            break;
                        }
                    }else{
                        cpt ++;
                    }
                }

            }
        }
        return firstParagraph;
    }
}