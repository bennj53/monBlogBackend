package com.whiterabbit.services;

import com.gargoylesoftware.htmlunit.html.*;
import com.whiterabbit.dto.InputDataLot;
import com.whiterabbit.dto.InputData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Class to read and extract data from https://www.lesnumeriques.com/actualites
 */
public class HtmlReaderLesNum implements HtmlReader {

    Logger log = LoggerFactory.getLogger(HtmlReaderLesNum.class);
    public static final String LESNUMS = "LesNumériques.com";

    @Override
    public InputDataLot readHtmlPage(String url) {
            HtmlPage htmlPage = this.getHtmlPage(url);
            InputDataLot inputDataLot = new InputDataLot();

            if(htmlPage != null) {
                List<HtmlElement> items = htmlPage.getByXPath("//section[@class='ln__lyt']");
                List<HtmlElement> ul = htmlPage.getByXPath("//ul[@class='row ln__list--16 ln__list--8--v-sm--mw ln__vr--16 ln__vr--8--v-sm--mw']");
                List<HtmlElement> li = htmlPage.getByXPath("//li[@class='col-xs-12']");

                if (li.isEmpty()) {
                    log.error("Les NUMS - No items found ! ");
                } else {

                    int cpt = 0;

                    for (HtmlElement htmlItem : li) {
                        String itemUrl = null;
                        String itemTitle = null;
                        String pathImg = null;
                        List<HtmlAnchor> itemAnchors = htmlItem.getByXPath(".//a");

                        for (HtmlAnchor anchor : itemAnchors) {
                            itemUrl = "https://www.lesnumeriques.com" + anchor.getHrefAttribute();
                            itemTitle = anchor.getAttribute("aria-label") != null
                                    && !anchor.getAttribute("aria-label").equals("") ?
                                    anchor.getAttribute("aria-label")
                                    : itemTitle;
                            log.info(String.format("Les NUMS - Title %s : %s --> Url : %s", ++cpt, itemTitle, itemUrl));


                            //List<HtmlArticle> imgAnchors = anchor.getByXPath(".//article");
                            List<HtmlSource> sources = anchor.getByXPath(".//source");

                            for (HtmlSource source : sources) {
                                pathImg = source.getAttribute("data-srcset") != null && !source.getAttribute("data-srcset").equals("") ? source.getAttribute("data-srcset") : pathImg;
                                log.info(String.format("Les NUMS - Img Path : %s%n", pathImg));
                            }
                            //System.out.println( String.format("Les NUMS - Img Path : %s%n", pathImg));
                            if (pathImg != null && !pathImg.equals(""))
                                break;
                        }
                        //créer l' Article
                        if (itemTitle != null && itemUrl != null && !itemTitle.equals("") && !itemUrl.equals("")) {
                            InputData inputData = new InputData();
                            inputData.setTitre(itemTitle);
                            inputData.setImg(pathImg);
                            inputData.setUrl(itemUrl);
                            inputData.setAuteur(LESNUMS);
                            //add resume article
                            String resume = null;
                            try {
                                resume = this.readHtmlPageResume(itemUrl);
                                inputData.setResume(resume);
                                log.info(String.format("Les NUMS - Resume : %s%n", inputData.getResume()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            inputDataLot.getInputDatas().add(inputData);
                        }
                    }
                }
            }else{
                log.error("Les NUMS - No web page found for url : " + url);
            }
        return inputDataLot;
    }

    @Override
    public String readHtmlPageResume(String url) throws IOException {
        HtmlPage page = this.getHtmlPage(url);
        String paragraphTextContent = null;

        if(page != null) {
           List<HtmlParagraph> paragraphs = page.getByXPath("//p[@class='ed__a-h ed__bdy__l']");
            if(paragraphs == null ||paragraphs.isEmpty()){
                log.error("LesNums - No p found ! ");
            }else{
                for(HtmlElement paragraph : paragraphs) {

                    paragraphTextContent = paragraph.getTextContent();

                    List<HtmlAnchor> itemAnchors = paragraph.getByXPath(".//a");

                    int cpt= 1;

                    for (HtmlAnchor anchor : itemAnchors) {
                        paragraphTextContent = paragraphTextContent + anchor.getTextContent();

                    }
                }
            }
        }else{
            log.error(url + " - resume page not found");
        }
        return paragraphTextContent;
    }
}
