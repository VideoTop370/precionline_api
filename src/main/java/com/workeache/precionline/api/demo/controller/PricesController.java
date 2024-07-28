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

@RestController
@RequestMapping("/prices")
public class PricesController {

    @Autowired
    private ApiReeService apiReeService;

    @Autowired
    private DataApiReeService dataApiReeService;

    @GetMapping("/actual")
    public ResponseEntity<?> getActualPrices() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin", "*");
        responseHeaders.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
        responseHeaders.set("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        responseHeaders.set("Access-Control-Allow-Credentials", "true");

        DataApiRee dataApiRee = dataApiReeService.getActualDayPrices();

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(dataApiRee.getData());
    }

    @GetMapping("/nextday")
    public ResponseEntity<?> getNextDayPrices() {
        DataApiRee dataApiRee = dataApiReeService.getNextDayPrices();
        return ResponseEntity.ok(dataApiRee.getData());
    }

    @GetMapping("/query")
    public ResponseEntity<?> getPricesByDate(@RequestParam String date) {
        // Implementa la lógica para obtener precios por fecha
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Método no implementado");
    }

    @GetMapping("/updateActualPrice")
    public ResponseEntity<String> updateActualPrice() {
        LocalDate actualDate = LocalDate.now();
        LocalDate nextDayDate = actualDate.plusDays(1);

        dataApiReeService.save(apiReeService.updatePrices(actualDate, nextDayDate));

        return ResponseEntity.ok("Precios día actual actualizados");
    }

    @GetMapping("/updateNextDayPrices")
    public ResponseEntity<String> updateNextDayPrices() {
        LocalDate actualDate = LocalDate.now().plusDays(1);
        LocalDate nextDayDate = actualDate.plusDays(1);

        dataApiReeService.save(apiReeService.updatePrices(actualDate, nextDayDate));

        return ResponseEntity.ok("Precios día siguiente actualizados");
    }
}
