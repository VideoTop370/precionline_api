package com.workeache.precionline.api.demo.controller;

import com.workeache.precionline.api.demo.persistence.entities.DataApiRee;
import com.workeache.precionline.api.demo.services.ApiReeService;
import com.workeache.precionline.api.demo.services.DataApiReeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

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
        DataApiRee dataApiRee =  dataApiReeService.getActualDayPrices();
        return ResponseEntity.ok(dataApiRee.getData());

    }

    @GetMapping("/nextday")
    public ResponseEntity<?> getNextDayPrices(){
        DataApiRee dataApiRee = dataApiReeService.getNextDayPrices();
        return ResponseEntity.ok(dataApiRee.getData());
    }

    @GetMapping("/query/{date}")
    public ResponseEntity<?> getPricesByDate(@PathVariable LocalDate localDate){
        DataApiRee dataApiRee = dataApiReeService.getPricesByDate(localDate);
        return ResponseEntity.ok(dataApiRee.getData());
    }


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
