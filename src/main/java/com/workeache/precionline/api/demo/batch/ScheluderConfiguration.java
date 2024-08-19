package com.workeache.precionline.api.demo.batch;

import com.workeache.precionline.api.demo.services.ApiReeService;
import com.workeache.precionline.api.demo.services.DataApiReeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


import java.time.LocalDate;
import java.time.LocalDateTime;


@Configuration
@EnableScheduling
public class ScheluderConfiguration {

    private final Logger logger = LoggerFactory.getLogger(ScheluderConfiguration.class);

    @Autowired
    private ApiReeService apiReeService;

    @Autowired
    private DataApiReeService dataApiReeService;

    @Scheduled(cron = "0 54 13 * * *")
    public void launchJob() throws Exception {

        logger.debug("Start price update " + LocalDateTime.now());

        LocalDate actualDate = LocalDate.now();
        LocalDate nextDayDate = actualDate.plusDays(1);
        dataApiReeService.save(apiReeService.updatePrices(actualDate, nextDayDate));

        logger.debug("End price update " + LocalDateTime.now());

    }


}
