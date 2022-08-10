package com.whiterabbit.services;

import com.whiterabbit.dto.InputDataLot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HtmlReaderServiceImplTest {

    @Test
    void readHtmlPage_Human_Coders_com() {
        HtmlReaderService htmlReaderService = new HtmlReaderServiceImpl();
        InputDataLot inputDataLot = htmlReaderService.readHtmlPage("https://news.humancoders.com/t/java");
        Assertions.assertNotNull(inputDataLot);
    }

    @Test
    void readHtmlPage_Medium_com() {
        HtmlReaderService htmlReaderService = new HtmlReaderServiceImpl();
        InputDataLot inputDataLot = htmlReaderService.readHtmlPage("https://medium.com/topic/programming");
        Assertions.assertNotNull(inputDataLot);
    }
}