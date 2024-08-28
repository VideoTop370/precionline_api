package com.workeache.precionline.api.demo.batch;

import com.workeache.precionline.api.demo.exceptions.DataNotUpdateException;
import com.workeache.precionline.api.demo.services.ApiReeService;
import com.workeache.precionline.api.demo.services.DataApiReeService;
import com.workeache.precionline.api.demo.utils.notifications.GotifyClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.workeache.precionline.api.demo.utils.notifications.Constant.*;


@Configuration
@EnableScheduling
@EnableRetry
public class ScheluderConfiguration {

    private final Logger logger = LoggerFactory.getLogger(ScheluderConfiguration.class);

    @Autowired
    private ApiReeService apiReeService;

    @Autowired
    private DataApiReeService dataApiReeService;

    @Autowired
    private GotifyClientService gotifyClientService;


    @Scheduled(cron = "${precionline.cron}")
    @Retryable(retryFor = DataNotUpdateException.class, maxAttemptsExpression  = "${precionline.retry.maxAttempts}", backoff = @Backoff(delayExpression  = "${precionline.retry.maxDelay}"))
    public void launchJob() throws IOException, InterruptedException {

        try {
            logger.info("Inicio de actualización de precios " + LocalDateTime.now());

            LocalDate actualDate = LocalDate.now().plusDays(1);
            LocalDate nextDayDate = actualDate.plusDays(1);
            dataApiReeService.save(apiReeService.updatePrices(actualDate, nextDayDate));

            actualDate = actualDate.plusDays(1);
            nextDayDate = nextDayDate.plusDays(1);
            dataApiReeService.save(apiReeService.updatePrices(actualDate, nextDayDate));

            logger.info("Fin de actualización de precios " + LocalDateTime.now());
            gotifyClientService.sendMessage(A003_SUCCESS);
        }catch (DataNotUpdateException dataNotUpdateException) {
            logger.error("Datos de precios no disponibles. Se reintentará en ${precionline.retry.maxDelay} milisegundos" );
            gotifyClientService.sendMessage(A004_ERROR);
            throw new DataNotUpdateException("Pruebas");
        }catch (Exception e){
            logger.error("Prices update scheduled failed " + LocalDateTime.now());
            gotifyClientService.sendMessage(A003_ERROR);
        }
    }

    @Recover
    void recover(DataNotUpdateException e) throws Exception {
        throw new Exception(e);
    }
}
