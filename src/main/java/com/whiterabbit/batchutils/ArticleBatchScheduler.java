package com.whiterabbit.batchutils;

import com.whiterabbit.dao.ArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ArticleBatchScheduler {
    private Logger log = LoggerFactory.getLogger(ArticleBatchScheduler.class);
    @Autowired
    protected JobLauncher jobLauncher;
    @Autowired
    private JobConfiguration job;
    @Autowired
    private ArticleRepository articleRepository;

    //@Scheduled(cron = "0 * * * * * ")
    //Launch every X ms
    @Scheduled(fixedDelayString = "${job.articles.periodicity}")
    public void schedule() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        log.error("START JOB ........");
        //vider base des articles dont auteur != bennj53
        articleRepository.deleteAll(articleRepository.findByAuteurNot("bennj53"));
        //lancer le job update articles listes
        JobExecution jobExecution = jobLauncher.run(job.articleJob(), new JobParametersBuilder()
                .addDate("date", new Date())
                .toJobParameters());

        log.error("JOB nom-du-job finished .... with status " + jobExecution.getExitStatus());
    }
}
