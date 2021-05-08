package com.whiterabbit.services;

import com.whiterabbit.dto.InputDataLot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HtmlReaderServiceImplTest {

    @Test
    void readHtmlPage() {
        HtmlReaderService htmlReaderService = new HtmlReaderServiceImpl();
        InputDataLot inputDataLot = htmlReaderService.readHtmlPage("https://news.humancoders.com/t/java");
        Assertions.assertNotNull(inputDataLot);
    }
}