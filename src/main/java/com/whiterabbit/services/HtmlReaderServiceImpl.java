package com.whiterabbit.services;

import com.whiterabbit.dto.InputDataLot;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Service
@Data
public class HtmlReaderServiceImpl implements HtmlReaderService {

    Logger log = LoggerFactory.getLogger(HtmlReaderServiceImpl.class);
    private HtmlReader htmlReader;
   /* private List<InputData> inputDataList;
    private InputDataLot convertedInputData;*/


   /* private HtmlPage getHtmlPage(String webSiteUrl) {
        HtmlPage page = null;
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        try {
            String searchUrl = webSiteUrl;//"https://www.journaldugeek.com/articles/";
            page = client.getPage(searchUrl);
        }catch (Exception e){
            e.printStackTrace();
            client.close();
        }

        return page;
    }*/

    @Override
    public InputDataLot readHtmlPage(String url) {
        InputDataLot inputDataLot = new InputDataLot();
        String classNameReader = null;

        //récupérer la classe de lecture de la page à instancier à partir de url dans properties
        String propertyKey = url.split("/")[2].trim();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = loader.getResourceAsStream("application.properties");
        Properties props = new Properties();
        try {
            props.load(inputStream);
            classNameReader = props.getProperty("class.to.read."+propertyKey);
            log.error("Property Key : class.to.read." + propertyKey);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //lancer la lecture de la page
        if(classNameReader != null){
            try {
                log.error("Class articleReader to instance : " + Class.forName(classNameReader).getName());
                htmlReader = (HtmlReader) Class.forName(classNameReader).newInstance();
                inputDataLot = htmlReader.readHtmlPage(url);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return inputDataLot;
    }
/*
    //TODO : déplacer code : créer classe dedier à chaque site
    private void readJdgPage(HtmlPage htmlPage) {

        List<HtmlElement> items = htmlPage.getByXPath("//section[@class='archive__content page__content']");

            if(items.isEmpty()){
                System.out.println("JDG - No items found ! ");
            }else{
                for(HtmlElement htmlItem : items){
                    List<HtmlAnchor> itemAnchors =  htmlItem.getByXPath(".//a");

                    int cpt = 0;

                    for (HtmlAnchor anchor : itemAnchors) {
                        String itemUrl = anchor.getHrefAttribute() ;
                        String itemTitle = anchor.getAttribute("title");
                        System.out.println( String.format("JDG - Title %s : %s --> Url : %s", ++cpt,itemTitle,itemUrl));


                        List<HtmlImage> imgAnchors = anchor.getByXPath(".//img");
                        for(HtmlImage img : imgAnchors){
                            String pathImg = img.getAttribute("data-srcset");
                            pathImg = pathImg.split(",",0)[0].trim();
                            System.out.println( String.format("JDG - Img Path : %s%n", pathImg));

                            //créer l' Article
                            InputData inputData = new InputData();
                            inputData.setTitre(itemTitle);
                            inputData.setImg(pathImg);
                            inputData.setUrl(itemUrl);
                            inputData.setAuteur("journaldugeek.com");
                            //add resume article
                            try {
                                String articleDetail = scrapingJDGArticleDetails(itemUrl);
                                inputData.setResume(articleDetail);
                                System.out.println( String.format("JDG - Resume : %s%n", inputData.getResume()));
                                //articleRepository.save(article);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            convertedInputData.getInputDatas().add(inputData);
                        }
                    }

                }
            }
    }

    private String scrapingJDGArticleDetails(String articleUrl) throws IOException {
        HtmlPage page = this.getHtmlPage(articleUrl);
        String firstParagraph = null;

        List<HtmlElement> items = page.getByXPath("//div[@class='single__post']");
        if(items.isEmpty()){
            System.out.println("JDG - No p found ! ");
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
    }*/
}
