package com.workeache.precionline.api.demo.controller;

import com.workeache.precionline.api.demo.persistence.entities.DataApiRee;
import com.workeache.precionline.api.demo.services.ApiReeService;
import com.workeache.precionline.api.demo.services.DataApiReeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/prices")
public class PricesController {

    @Autowired
    ApiReeService apiReeService;

    @Autowired
    DataApiReeService dataApiReeService;

    @GetMapping("/actual")
    public ResponseEntity<?> getActualPrices()
    {

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin","*");
        responseHeaders.set("Access-Control-Allow-Methods","GET,PUT,POST,DELETE,PATCH,OPTIONS");

        DataApiRee dataApiRee =  dataApiReeService.getActualDayPrices();

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(dataApiRee.getData());

    }

    @GetMapping("/nextday")
    public ResponseEntity<?> getNextDayPrices(){
        DataApiRee dataApiRee = dataApiReeService.getNextDayPrices();
        return ResponseEntity.ok(dataApiRee.getData());
    }

    @GetMapping("/query")
    public ResponseEntity<?> getPricesByDate(@RequestParam String date){
        /*final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
        final LocalDate dt = dtf.parseLocalDate(yourinput);
        DataApiRee dataApiRee = dataApiReeService.getPricesByDate(localDate);
        return null;*/
        //return ResponseEntity.ok(dataApiRee.getData());
        return null;
    }

    //@GetMapping("/updateDate/{date}")

    /*@GetMapping("/updateActualMonth")
    public String updateActualMonth(){

        LocalDate intitalDate = LocalDate.of(2024, 06, 01);
        LocalDate nextDayDate = intitalDate.plusDays(1);

        while(intitalDate.getDayOfMonth()!=30){
            dataApiReeService.save(apiReeService.updatePrices(intitalDate, nextDayDate));
            intitalDate = nextDayDate;
            nextDayDate = intitalDate.plusDays(1);
        }

        return "Precios en rango de mes actualizados";
    }*/


    @GetMapping("/updateActualPrice")
    public String updateActualPrice(){

        LocalDate actualDate = LocalDate.now();
        LocalDate nextDayDate = actualDate.plusDays(1);

        dataApiReeService.save(apiReeService.updatePrices(actualDate, nextDayDate));

        return "Precios día actual actualizados";
    }


    @GetMapping("/updateNextDayPrices")
    public String updateNextDayPrices(){

        LocalDate actualDate = LocalDate.now().plusDays(1);
        LocalDate nextDayDate = actualDate.plusDays(1);

        dataApiReeService.save(apiReeService.updatePrices(actualDate, nextDayDate));

        return "Precios día siguiente actualizados";
    }

}
