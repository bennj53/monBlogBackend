package com.whiterabbit.services;

import com.whiterabbit.dto.InputDataLot;

public interface HtmlReaderService {
    InputDataLot readHtmlPage(String url);
}
