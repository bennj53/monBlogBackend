package com.whiterabbit.services;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.whiterabbit.dao.ArticleRepository;
import com.whiterabbit.entities.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    public static final String JDG = "journaldugeek.com";
    public static final String LESNUMS = "LesNumériques.com";
    public static final String ZERO1NET = "01net.com";
    public static final String MAC4EVER = "mac4ever.com";
    public static final String FRANDROID = "frandroid.com";


    @Autowired
    ArticleRepository articleRepository;

    @Override
    public void startScraping(String webSite) {
        switch(webSite){
            case "JDG" :
                scrapingJDG();
                break;
            case "LESNUMS" :
                scrapingLesNum();
                break;
            case "01NET" :
                scraping01Net();
                break;
            case "MAC4EVER" :
                scrapingMac4Ever();
                break;
            case "FRANDROID" :
                scrapingFrAndroid();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + webSite);
        }
    }

    public void scrapingJDG(){
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        try {
            String searchUrl = "https://www.journaldugeek.com/articles/";
            HtmlPage page = client.getPage(searchUrl);

            List<HtmlElement> items = page.getByXPath("//section[@class='archive__content page__content']");

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
                            Article article = new Article();
                            article.setTitre(itemTitle);
                            article.setImg(pathImg);
                            article.setUrl(itemUrl);
                            article.setAuteur(JDG);
                            //add resume article
                            scrapingJDGArticleDetails(itemUrl, client, article);
                            System.out.println( String.format("JDG - Resume : %s%n", article.getResume()));
                            articleRepository.save(article);
                        }
                    }

                }
            }


        }catch(Exception e){
            e.printStackTrace();
            client.close();
        }

        client.close();
    }

    public void scrapingJDGArticleDetails(String articleUrl, WebClient client, Article article) throws IOException {
        HtmlPage page = client.getPage(articleUrl);

        List<HtmlElement> items = page.getByXPath("//div[@class='single__post']");
        if(items.isEmpty()){
            System.out.println("JDG - No p found ! ");
        }else{
            for(HtmlElement htmlItem : items) {
                List<HtmlParagraph> itemParagraph = htmlItem.getByXPath(".//p");

                int cpt= 1;
                String firstParagraph = null;

                for (HtmlParagraph paragraph : itemParagraph) {
                    if (cpt == 1){
                        firstParagraph = paragraph.getTextContent();
                        if(firstParagraph != null && !firstParagraph.equals("")) {
                            article.setResume(firstParagraph);
                            break;
                        }
                    }else{
                        cpt ++;
                    }
                }

            }
        }
    }
    public void scrapingLesNum(){
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        try {
            String searchUrl = "https://www.lesnumeriques.com/actualites";
            HtmlPage page = client.getPage(searchUrl);

            List<HtmlElement> items = page.getByXPath("//section[@class='ln__lyt']");
            List<HtmlElement> ul = page.getByXPath("//ul[@class='row ln__list--16 ln__list--8--v-sm--mw ln__vr--16 ln__vr--8--v-sm--mw']");
            List<HtmlElement> li = page.getByXPath("//li[@class='col-xs-12']");

            if(li.isEmpty()){
                System.out.println("Les NUMS - No items found ! ");
            }else{

                int cpt = 0;

                for(HtmlElement htmlItem : li){
                    String itemUrl = null;
                    String itemTitle = null;
                    String pathImg = null;
                    List<HtmlAnchor> itemAnchors =  htmlItem.getByXPath(".//a");

                    for (HtmlAnchor anchor : itemAnchors) {
                        itemUrl = "https://www.lesnumeriques.com" + anchor.getHrefAttribute() ;
                        itemTitle = anchor.getAttribute("aria-label") != null
                                && !anchor.getAttribute("aria-label").equals("") ?
                                anchor.getAttribute("aria-label")
                                : itemTitle ;
                        System.out.println( String.format("Les NUMS - Title %s : %s --> Url : %s", ++cpt,itemTitle,itemUrl));


                        //List<HtmlArticle> imgAnchors = anchor.getByXPath(".//article");
                        List<HtmlSource> sources = anchor.getByXPath(".//source");

                        for(HtmlSource source : sources){
                            pathImg = source.getAttribute("data-srcset")!=null && !source.getAttribute("data-srcset").equals("") ? source.getAttribute("data-srcset") : pathImg;
                            System.out.println( String.format("Les NUMS - Img Path : %s%n", pathImg));
                        }
                        //System.out.println( String.format("Les NUMS - Img Path : %s%n", pathImg));
                        if(pathImg != null && !pathImg.equals(""))
                            break;
                    }
                    //créer l' Article
                    if(itemTitle != null && itemUrl != null && !itemTitle.equals("") && !itemUrl.equals("")){
                        Article article = new Article();
                        article.setTitre(itemTitle);
                        article.setImg(pathImg);
                        article.setUrl(itemUrl);
                        article.setAuteur(LESNUMS);
                        //add resume article
                        scrapingLesNumArticleDetails(itemUrl, client, article);
                        System.out.println( String.format("Les NUMS - Resume : %s%n", article.getResume()));
                        articleRepository.save(article);
                    }
                }
            }


        }catch(Exception e){
            e.printStackTrace();
            client.close();
        }

        client.close();
    }

    public void scrapingLesNumArticleDetails(String articleUrl, WebClient client, Article article) throws IOException {
        HtmlPage page = client.getPage(articleUrl);

        List<HtmlParagraph> paragraphs = page.getByXPath("//p[@class='ed__a-h ed__bdy__l']");
        if(paragraphs.isEmpty()){
            System.out.println("LesNums - No p found ! ");
        }else{
            for(HtmlElement paragraph : paragraphs) {

                String paragraphTextContent = paragraph.getTextContent();

                List<HtmlAnchor> itemAnchors = paragraph.getByXPath(".//a");

                int cpt= 1;
                String firstParagraph = null;

                for (HtmlAnchor anchor : itemAnchors) {
                        paragraphTextContent = paragraphTextContent + anchor.getTextContent();

                }
                article.setResume(paragraphTextContent);
            }
        }
    }

    public void scraping01Net(){

        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        try {
            String searchUrl = "https://www.01net.com/actualites/";
            HtmlPage page = client.getPage(searchUrl);

            List<HtmlArticle> articles = page.getByXPath("//article[@class='timeline-panel bg-color-1 row no-margin']");
            if(articles.isEmpty()){
                System.out.println("01Net - No article found ! ");
            }else{
                int cpt = 0;
                String articleTextContent =null;
                String articleUrl = null;
                String pathImg = null;
                System.out.println("01Net - article found ! ");

                for (HtmlArticle article: articles){
                    List<HtmlHeading2> itemH2s = article.getByXPath(".//h2");
                    articleTextContent =  itemH2s != null && itemH2s.size() >=1 ? itemH2s.get(0).getTextContent() : null;
                    List<HtmlAnchor> itemAnchors = article.getByXPath(".//a");
                    articleUrl = itemAnchors != null && itemAnchors.size()>=1 ? itemAnchors.get(0).getHrefAttribute() : null;
                    List<HtmlImage> itemImgs = article.getByXPath(".//img");
                    pathImg = itemImgs != null && itemImgs.size() >=1 ? itemImgs.get(0).getAttribute("data-original") : null;
                    System.out.println(String.format("01Net - Article %s --> Titre : %s // Url : %s", ++cpt, articleTextContent, articleUrl));
                    System.out.println(String.format("01Net - Img : %s", pathImg));

                    //créer l' Article
                    Article articl = new Article();
                    articl.setTitre(articleTextContent);
                    articl.setImg(pathImg);
                    articl.setUrl(articleUrl);
                    articl.setAuteur(ZERO1NET);
                    //add resume article
                    scraping01NetArticleDetails("https:" + articleUrl, client, articl);
                    System.out.println(String.format("01Net - Resume : %s%n", articl.getResume()));
                    articleRepository.save(articl);

                }


            }

        }catch(Exception e){
            e.printStackTrace();
            client.close();
        }

        client.close();
    }

    public void scraping01NetArticleDetails(String articleUrl, WebClient client, Article article) throws IOException {
        HtmlPage page = client.getPage(articleUrl);

        List<HtmlHeading2> heading2s = page.getByXPath("//h2[@class='title-large padding-bottomx2 blocx3 border-b-s']");
        if(heading2s.isEmpty()){
            System.out.println("01Net - No h2 found ! ");
        }else{
            String resume = null;
            for(HtmlElement heading2 : heading2s) {
                resume = heading2.getTextContent();

            }
            article.setResume(resume);
        }
    }

    public void scrapingMac4Ever(){

        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        try {
            String searchUrl = "https://www.mac4ever.com/dossiers";
            HtmlPage page = client.getPage(searchUrl);

            List<HtmlDivision> divisions = page.getByXPath("//div[@class='zone-dossiers']");
            if(divisions.isEmpty()){
                System.out.println("Mac4Ever - No division found ! ");
            }else{
                int cpt = 0;
                String articleTextContent =null;
                String articleUrl = null;
                String pathImg = null;
                String resume = null;
                System.out.println("Mac4Ever - article found ! ");

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
                        System.out.println(String.format("Mac4Ever - Article %s --> Titre : %s // Url : %s", ++cpt, articleTextContent, articleUrl));
                        System.out.println(String.format("Mac4Ever - Img : %s", pathImg));
                        System.out.println(String.format("Mac4Ever - Resume : %s%n", resume));

                        //créer l' Article
                        Article articl = new Article();
                        articl.setTitre(articleTextContent);
                        articl.setImg("https://www.mac4ever.com" + pathImg);
                        articl.setUrl("https://www.mac4ever.com" + articleUrl);
                        articl.setAuteur(MAC4EVER);
                        //add resume article
                        articl.setResume(resume);

                        articleRepository.save(articl);
                    }


                }


            }

        }catch(Exception e){
            e.printStackTrace();
            client.close();
        }

        client.close();
    }

    public void scrapingMac4EverArticleDetails(String articleUrl, WebClient client, Article article) throws IOException {

    }

    public void scrapingFrAndroid(){
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        try{
            String searchUrl = "https://www.frandroid.com/actualites";
            HtmlPage page = client.getPage(searchUrl);

            List<HtmlDivision> divisions = page.getByXPath("//div[@class='columns is-vcentered is-mobile is-relative']");
            if(divisions.isEmpty()){
                System.out.println("frandroid - No division found ! ");
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
                    System.out.println(String.format("frandroid - Article %s --> Category : %s // Url Category : %s", ++cpt, articleCategory, articleCategoryUrl));
                    System.out.println(String.format("frandroid - Article %s --> Titre : %s // Url : %s", cpt, articleTitle, articleUrl));
                    System.out.println(String.format("frandroid - Article %s --> Date : %s",  cpt,articleDate));
                    System.out.println(String.format("frandroid - Article %s --> Img : %s",  cpt,pathImg));


                    //create article
                    Article articl = new Article();
                    articl.setTitre(articleTitle);
                    articl.setImg(pathImg);
                    articl.setUrl(articleUrl);
                    articl.setDatePublication(articleDate);
                    articl.setAuteur(FRANDROID);
                    //add resume article
                    this.scrapingFrAndroidArticleDetails(articleUrl, client, articl);
                    System.out.println(String.format("frandroid - Article %s --> Resume : %s%n",  cpt, articl.getResume()));

                    //save article
                    articleRepository.save(articl);

                }
            }
        }catch(Exception e){
            e.printStackTrace();
            client.close();
        }
    }

    public void scrapingFrAndroidArticleDetails(String articleUrl, WebClient client, Article article) throws IOException {
        HtmlPage page = client.getPage(articleUrl);

        List<HtmlParagraph> paragraphArticleResume = page.getByXPath(".//p[@class='chapo']");
        String articleResume = paragraphArticleResume != null && paragraphArticleResume.size()>=1 ? paragraphArticleResume.get(0).getTextContent() : null;

        if(articleResume == null){
            System.out.println("frandroid - No article resume found ! ");
        }else{
            article.setResume(articleResume);
        }
    }

    public void scrapingIphonFr(){

    }

    public void scrapingIphonFrArticleDetails(String articleUrl, WebClient client, Article article) throws IOException {

    }
}
