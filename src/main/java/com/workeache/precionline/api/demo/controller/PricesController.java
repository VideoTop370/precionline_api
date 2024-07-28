package com.workeache.precionline.api.demo.controller;

import com.workeache.precionline.api.demo.persistence.entities.DataApiRee;
import com.workeache.precionline.api.demo.services.ApiReeService;
import com.workeache.precionline.api.demo.services.DataApiReeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/prices")
public class PricesController {

    @Autowired
    ApiReeService apiReeService;

    @Autowired
    DataApiReeService dataApiReeService;

    @CrossOrigin(origins = "http://localhost:3000")  // Configuración específica para el controlador
    @GetMapping("/actual")
    public ResponseEntity<?> getActualPrices() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin", "*");
        responseHeaders.set("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,PATCH,OPTIONS");
        responseHeaders.set("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        responseHeaders.set("Access-Control-Allow-Credentials", "true");

        DataApiRee dataApiRee = dataApiReeService.getActualDayPrices();

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(dataApiRee.getData());
    }

    @CrossOrigin(origins = "http://localhost:3000")  // Configuración específica para el controlador
    @GetMapping("/nextday")
    public ResponseEntity<?> getNextDayPrices() {
        DataApiRee dataApiRee = dataApiReeService.getNextDayPrices();
        return ResponseEntity.ok(dataApiRee.getData());
    }

    @CrossOrigin(origins = "http://localhost:3000")  // Configuración específica para el controlador
    @GetMapping("/query")
    public ResponseEntity<?> getPricesByDate(@RequestParam String date) {
        // Código pendiente para manejar la fecha
        return null;
    }

    @CrossOrigin(origins = "http://localhost:3000")  // Configuración específica para el controlador
    @GetMapping("/updateActualPrice")
    public String updateActualPrice() {
        LocalDate actualDate = LocalDate.now();
        LocalDate nextDayDate = actualDate.plusDays(1);
        dataApiReeService.save(apiReeService.updatePrices(actualDate, nextDayDate));
        return "Precios día actual actualizados";
    }

    @CrossOrigin(origins = "http://localhost:3000")  // Configuración específica para el controlador
    @GetMapping("/updateNextDayPrices")
    public String updateNextDayPrices() {
        LocalDate actualDate = LocalDate.now().plusDays(1);
        LocalDate nextDayDate = actualDate.plusDays(1);
        dataApiReeService.save(apiReeService.updatePrices(actualDate, nextDayDate));
        return "Precios día siguiente actualizados";
    }
}
