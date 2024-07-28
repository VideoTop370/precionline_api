package com.workeache.precionline.api.demo.controller;

import com.workeache.precionline.api.demo.persistence.entities.DataApiRee;
import com.workeache.precionline.api.demo.services.ApiReeService;
import com.workeache.precionline.api.demo.services.DataApiReeService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @CrossOrigin(origins = "https://precionline.com")  // Permite CORS para este origen
    @GetMapping("/actual")
    public ResponseEntity<?> getActualPrices() {
        DataApiRee dataApiRee = dataApiReeService.getActualDayPrices();
        return ResponseEntity.ok(dataApiRee.getData());
    }

    @CrossOrigin(origins = "https://precionline.com")  // Permite CORS para este origen
    @GetMapping("/nextday")
    public ResponseEntity<?> getNextDayPrices() {
        DataApiRee dataApiRee = dataApiReeService.getNextDayPrices();
        return ResponseEntity.ok(dataApiRee.getData());
    }

    @CrossOrigin(origins = "https://precionline.com")  // Permite CORS para este origen
    @GetMapping("/query")
    public ResponseEntity<?> getPricesByDate(@RequestParam String date) {
        return ResponseEntity.ok("Not implemented yet");
    }

    @CrossOrigin(origins = "https://precionline.com")  // Permite CORS para este origen
    @GetMapping("/updateActualPrice")
    public String updateActualPrice() {
        LocalDate actualDate = LocalDate.now();
        LocalDate nextDayDate = actualDate.plusDays(1);
        dataApiReeService.save(apiReeService.updatePrices(actualDate, nextDayDate));
        return "Precios del día actual actualizados";
    }

    @CrossOrigin(origins = "https://precionline.com")  // Permite CORS para este origen
    @GetMapping("/updateNextDayPrices")
    public String updateNextDayPrices() {
        LocalDate actualDate = LocalDate.now().plusDays(1);
        LocalDate nextDayDate = actualDate.plusDays(1);
        dataApiReeService.save(apiReeService.updatePrices(actualDate, nextDayDate));
        return "Precios del día siguiente actualizados";
    }
}
