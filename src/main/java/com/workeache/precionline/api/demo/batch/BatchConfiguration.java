package com.workeache.precionline.api.demo.batch;

import com.workeache.precionline.api.demo.persistence.entities.DataApiRee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.core.launch.JobLauncher;


import java.util.Date;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


@Configuration
@EnableScheduling
public class BatchConfiguration {

    private final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;


    private AtomicInteger batchRunCounter = new AtomicInteger(0);

    private final Map<Object, ScheduledFuture<?>> scheduledTasks = new IdentityHashMap<>();

    @Scheduled(cron = "0 54 13 * * *")
    public void launchJob() throws Exception {
        Date date = new Date();
        logger.debug("scheduler starts at " + date);

        JobExecution jobExecution = jobLauncher.run(updatePricesJob(jobRepository, transactionManager), new JobParametersBuilder().addDate("launchDate", date)
                .toJobParameters());
        batchRunCounter.incrementAndGet();
        logger.debug("Batch job ends with status as " + jobExecution.getStatus());

    }

    @Bean
    public TaskScheduler poolScheduler() {
        return new CustomTaskScheduler();
    }

    private class CustomTaskScheduler extends ThreadPoolTaskScheduler {

        private static final long serialVersionUID = -7142624085505040603L;

        @Override
        public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
            ScheduledFuture<?> future = super.scheduleAtFixedRate(task, period);

            ScheduledMethodRunnable runnable = (ScheduledMethodRunnable) task;
            scheduledTasks.put(runnable.getTarget(), future);

            return future;
        }

    }

    public void cancelFutureSchedulerTasks() {
        scheduledTasks.forEach((k, v) -> {
            if (k instanceof BatchConfiguration) {
                v.cancel(false);
            }
        });
    }


    @Bean
    public ItemReader<List<DataApiRee>> reader(){
        return new ReaderApiRee();
    }

    @Bean
    public ItemWriter<? super Object> writer(){
        return items -> {
          logger.debug("writer..." + items.size());
        };
    }

    @Bean
    public Job updatePricesJob(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new JobBuilder("updatePricesJob", jobRepository)
                .start(step1(jobRepository,transactionManager))
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("step1", jobRepository)
                .chunk(2, transactionManager)
                .reader(reader())
                .writer(writer())
                .build();
    }
}
