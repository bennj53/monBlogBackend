package com.whiterabbit.services;

import com.gargoylesoftware.htmlunit.html.*;
import com.whiterabbit.dto.InputData;
import com.whiterabbit.dto.InputDataLot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Class to read and extract data from https://www.mac4ever.com/dossiers
 */
public class HtmlReaderMac4Ever implements HtmlReader {

    Logger log = LoggerFactory.getLogger(HtmlReaderMac4Ever.class);
    public static final String MAC4EVER = "mac4ever.com";

    @Override
    public InputDataLot readHtmlPage(String url) {

            HtmlPage page = this.getHtmlPage(url);
            InputDataLot inputDataLot = new InputDataLot();

            List<HtmlDivision> divisions = page.getByXPath("//div[@class='zone-dossiers']");
            if(divisions.isEmpty()){
                log.error("Mac4Ever - No division found ! ");
            }else{
                int cpt = 0;
                String articleTextContent =null;
                String articleUrl = null;
                String pathImg = null;
                String resume = null;
                log.info("Mac4Ever - article found ! ");

                for (HtmlDivision division: divisions){
                    List<HtmlDivision> divisionArticle = division.getByXPath(".//div[@class='last-dossier-right']");
                    if(divisionArticle != null && divisionArticle.size() >=1){
                        List<HtmlAnchor> itemAnchors = divisionArticle.get(0).getByXPath(".//a");
                        articleTextContent = itemAnchors != null && itemAnchors.size()>=1 ? itemAnchors.get(0).getTextContent():null;
                        articleUrl = itemAnchors != null && itemAnchors.size()>=1 ? itemAnchors.get(0).getHrefAttribute():null;
                        List<HtmlParagraph> itemParagraphs = divisionArticle.get(0).getByXPath(".//p");
                        resume = itemParagraphs != null && itemParagraphs.size()>=1 ? itemParagraphs.get(0).getTextContent() : null;

                        List<HtmlImage> img = division.getByXPath(".//img");
                        pathImg = img != null && img.size()>=1 ? img.get(0).getSrcAttribute() : null;
                        log.info(String.format("Mac4Ever - Article %s --> Titre : %s // Url : %s", ++cpt, articleTextContent, articleUrl));
                        log.info(String.format("Mac4Ever - Img : %s", pathImg));
                        log.info(String.format("Mac4Ever - Resume : %s%n", resume));

                        //créer l' Article
                        InputData inputData = new InputData();
                        inputData.setTitre(articleTextContent);
                        inputData.setImg("https://www.mac4ever.com" + pathImg);
                        inputData.setUrl("https://www.mac4ever.com" + articleUrl);
                        inputData.setAuteur(MAC4EVER);
                        //add resume article
                        inputData.setResume(resume);

                        inputDataLot.getInputDatas().add(inputData);
                    }
                }
            }
            return inputDataLot;
    }

    @Override
    public String readHtmlPageResume(String url) throws IOException {
        return "not implemented";
    }
}
