package com.workeache.precionline.api.demo.controller;

import com.workeache.precionline.api.demo.persistence.entities.DataApiRee;
import com.workeache.precionline.api.demo.services.ApiReeService;
import com.workeache.precionline.api.demo.services.DataApiReeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/prices")
@CrossOrigin(origins = "*", allowedHeaders = "*", allowCredentials = "true")
public class PricesController {

    @Autowired
    ApiReeService apiReeService;

    @Autowired
    DataApiReeService dataApiReeService;

    @GetMapping("/actual")
    public ResponseEntity<?> getActualPrices() {
        try {
            DataApiRee dataApiRee = dataApiReeService.getActualDayPrices();
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Access-Control-Allow-Origin", "*");
            responseHeaders.set("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,PATCH,OPTIONS");
            responseHeaders.set("Access-Control-Allow-Headers", "Origin,X-Requested-With,Content-Type,Accept");
            responseHeaders.set("Access-Control-Allow-Credentials", "true");

            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(dataApiRee.getData());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener los precios: " + e.getMessage());
        }
    }

    @GetMapping("/nextday")
    public ResponseEntity<?> getNextDayPrices() {
        try {
            DataApiRee dataApiRee = dataApiReeService.getNextDayPrices();
            return ResponseEntity.ok(dataApiRee.getData());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener los precios del siguiente día: " + e.getMessage());
        }
    }

    @GetMapping("/query")
    public ResponseEntity<?> getPricesByDate(@RequestParam String date) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(date, dtf);
            DataApiRee dataApiRee = dataApiReeService.getPricesByDate(localDate);
            return ResponseEntity.ok(dataApiRee.getData());
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Fecha inválida: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener los precios por fecha: " + e.getMessage());
        }
    }

    @GetMapping("/updateActualPrice")
    public ResponseEntity<String> updateActualPrice() {
        try {
            LocalDate actualDate = LocalDate.now();
            LocalDate nextDayDate = actualDate.plusDays(1);
            dataApiReeService.save(apiReeService.updatePrices(actualDate, nextDayDate));
            return ResponseEntity.ok("Precios día actual actualizados");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar los precios del día actual: " + e.getMessage());
        }
    }

    @GetMapping("/updateNextDayPrices")
    public ResponseEntity<String> updateNextDayPrices() {
        try {
            LocalDate actualDate = LocalDate.now().plusDays(1);
            LocalDate nextDayDate = actualDate.plusDays(1);
            dataApiReeService.save(apiReeService.updatePrices(actualDate, nextDayDate));
            return ResponseEntity.ok("Precios día siguiente actualizados");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar los precios del día siguiente: " + e.getMessage());
        }
    }
}
