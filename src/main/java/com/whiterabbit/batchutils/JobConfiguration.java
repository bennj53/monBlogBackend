package com.whiterabbit.batchutils;

import com.whiterabbit.dto.InputDataLot;
import com.whiterabbit.entities.ArticleLot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class JobConfiguration extends DefaultBatchConfigurer {

    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;
    @Autowired private ItemReader<InputDataLot> itemReader;
    @Autowired private ItemWriter<ArticleLot> itemWriter;
    @Autowired private ItemProcessor<InputDataLot, ArticleLot> itemProcessor;
    private Logger log = LoggerFactory.getLogger(JobConfiguration.class);


    @Bean
    public Job articleJob(){
        log.info("enter in articleJob()");
        Step step1=stepBuilderFactory.get("nom-du-step")
                .<InputDataLot, ArticleLot>chunk(1)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();

        return jobBuilderFactory.get("nom-du-job")
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }


}
