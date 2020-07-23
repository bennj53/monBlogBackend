package com.whiterabbit.batchutils;

import com.whiterabbit.dto.InputDataLot;
import com.whiterabbit.services.HtmlReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

@Component
@Configuration
@PropertySource("classpath:application.properties")
@StepScope
public class HtmlPageItemReader implements ItemReader<InputDataLot> {
    Logger log = LoggerFactory.getLogger(HtmlPageItemReader.class);
    @Autowired
    private HtmlReaderService htmlReaderService;
    private String[] urls;
    private String name = "HtmlPageReader1";
    static int index = 0;

    //Used in addition of @PropertySource
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public HtmlPageItemReader() {
        if(this.urls == null){
            this.urls = getUrlSources();
        }

        if (index > 0){
            this.resetIndex();
        }
        log.info("Url sources : " + Arrays.toString(this.urls));
    }

    private String[] getUrlSources(){
        //récupérer les url des sources dans properties
        String[] urlSources = new String[1];
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = loader.getResourceAsStream("application.properties");
        Properties props = new Properties();
        try {
            props.load(inputStream);
            String urlSourcesStr = props.getProperty("input.url.article.sources");
            log.info("Property Key : class.to.read." + urlSourcesStr);
            urlSources = urlSourcesStr != null ? urlSourcesStr.split(";"): new String[1];
            log.info("Sources Loaded from properties" + Arrays.toString(urlSources));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return urlSources;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public InputDataLot read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (this.urls == null || index>=this.urls.length) {
            log.info("----->ItemReader : url is null or empty or index >= url length");
            this.resetIndex();
            return null;
        }
        log.error("----->ItemReader : " + this.urls[index]);
        InputDataLot inputDataLot = htmlReaderService.readHtmlPage("https://" + this.urls[index]);
        index ++;
        return inputDataLot;
    }

    public void resetIndex(){
        index = 0;
    }

}
