package com.whiterabbit.utils;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.whiterabbit.dto.InputDataLot;

import java.io.IOException;

public interface HtmlReader {
    InputDataLot readHtmlPage(String url);
    String readHtmlPageResume(String url) throws IOException;

    default HtmlPage getHtmlPage(String url) {
        HtmlPage page = null;
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        try {
            String searchUrl = url;
            page = client.getPage(searchUrl);
        }catch (Exception e){
            e.printStackTrace();
            client.close();
        }
        client.close();
        return page;
    }

}
