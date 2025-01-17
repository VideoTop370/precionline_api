package com.workeache.precionline.api.demo.controller;


import com.workeache.precionline.api.demo.persistence.entities.DataApiReeEntity;
import com.workeache.precionline.api.demo.services.ApiReeService;
import com.workeache.precionline.api.demo.services.DataApiReeService;
import com.workeache.precionline.api.demo.utils.annotations.WithRateLimitProtection;
import com.workeache.precionline.api.demo.utils.notifications.GotifyClientService;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.workeache.precionline.api.demo.utils.notifications.Constant.*;


@CrossOrigin(origins = {
        "${precionline.origins}"
}) // Permite CORS para este origen
@RestController
@RequestMapping("/prices")
public class PricesController {

    @Autowired
    private ApiReeService apiReeService;

    @Autowired
    private DataApiReeService dataApiReeService;

    @Autowired
    private GotifyClientService gotifyClientService;


    private final Logger logger = LoggerFactory.getLogger(PricesController.class);

    @WithRateLimitProtection
    @GetMapping("/actual")
    public ResponseEntity<?> getActualPrices(HttpServletRequest request) {

        try {
            DataApiReeEntity dataApiReeEntity = dataApiReeService.getActualDayPrices();
            return ResponseEntity.ok(dataApiReeEntity.getData());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener los precios del día actual: " + e.getMessage());
        }
    }

    @WithRateLimitProtection
    @GetMapping("/nextday")
    public ResponseEntity<?> getNextDayPrices(HttpServletRequest request) {

        try {
            DataApiReeEntity dataApiReeEntity = dataApiReeService.getNextDayPrices();
            return ResponseEntity.ok(dataApiReeEntity.getData());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener los precios del siguiente día: " + e.getMessage());
        }
    }

    @WithRateLimitProtection
    @GetMapping("/query")
    public ResponseEntity<?> getPricesByDate(@RequestParam String date, HttpServletRequest request) {

        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(date, dtf);
            DataApiReeEntity dataApiReeEntity = dataApiReeService.getPricesByDate(localDate);
            return ResponseEntity.ok(dataApiReeEntity.getData());
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Fecha inválida: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener los precios por fecha: " + e.getMessage());
        }
    }

    @WithRateLimitProtection
    @GetMapping("/updateActualPrice")
    public ResponseEntity<?> updateActualPrice(HttpServletRequest request) throws IOException, InterruptedException {

        try {
            LocalDate actualDate = LocalDate.now(ZoneId.of("Europe/Madrid"));
            LocalDate nextDayDate = actualDate.plusDays(1);
            dataApiReeService.save(apiReeService.updatePrices(actualDate, nextDayDate, false));

            gotifyClientService.sendMessage(A001_SUCCESS);

            return ResponseEntity.ok("Precios día actual actualizados");
        } catch (Exception e) {
            logger.debug(e.getMessage());
            gotifyClientService.sendMessage(A001_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar los precios del día actual: " + e.getMessage());

        }
    }

    @WithRateLimitProtection
    @GetMapping("/updateNextDayPrices")
    public ResponseEntity<?> updateNextDayPrices(HttpServletRequest request) throws IOException, InterruptedException {

        try {
            LocalDate actualDate = LocalDate.now(ZoneId.of("Europe/Madrid")).plusDays(1);
            LocalDate nextDayDate = actualDate.plusDays(1);
            dataApiReeService.save(apiReeService.updatePrices(actualDate, nextDayDate,false));

            actualDate = actualDate.plusDays(1);
            nextDayDate = nextDayDate.plusDays(1);
            dataApiReeService.save(apiReeService.updatePrices(actualDate, nextDayDate,false));

            gotifyClientService.sendMessage(A002_SUCCESS);

            return ResponseEntity.ok("Precios día siguiente actualizados");
        } catch (Exception e) {
            logger.debug(e.getMessage());
            gotifyClientService.sendMessage(A002_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar los precios del día actual: " + e.getMessage());
        }
    }

    @Profile("dev")
    @GetMapping("/updateNextDayPricesTest")
    public ResponseEntity<?> updateNextDayPricesTest(HttpServletRequest request) throws IOException, InterruptedException {

        try {
            LocalDate actualDate = LocalDate.now(ZoneId.of("Europe/Madrid")).plusDays(1);
            LocalDate nextDayDate = actualDate.plusDays(1);
            dataApiReeService.save(apiReeService.updatePrices(actualDate, nextDayDate,false));

            actualDate = actualDate.plusDays(1);
            nextDayDate = nextDayDate.plusDays(1);
            dataApiReeService.save(apiReeService.updatePrices(actualDate, nextDayDate,false));

            gotifyClientService.sendMessage(A002_SUCCESS);

            return ResponseEntity.ok("Precios día siguiente actualizados");
        } catch (Exception e) {
            logger.debug(e.getMessage());
            gotifyClientService.sendMessage(A002_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar los precios del día actual: " + e.getMessage());
        }
    }

    //19-08-2024 -> Se quita este método para evitar problemas de seguridad y llenen la bbdd
    //28-08-2024 -> Dejo solo para el entorno de desarrollo
    @Profile("dev")
    @GetMapping("/updateAllMonth")
    public ResponseEntity<?> updateActualMonth(@RequestParam int month, @RequestParam int year, HttpServletRequest request) {


        try {
            LocalDate intitalDate = LocalDate.of(year, month, 1);
            LocalDate nextDayDate = intitalDate.plusDays(1);
            do {
                dataApiReeService.save(apiReeService.updatePrices(intitalDate, nextDayDate, false));
                intitalDate = nextDayDate;
                nextDayDate = intitalDate.plusDays(1);
            } while (intitalDate.getMonth().getValue() == month);

            return ResponseEntity.ok("Precios del mes actualizados");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar los precios de todo el mes: " + e.getMessage());
        }
    }


}
