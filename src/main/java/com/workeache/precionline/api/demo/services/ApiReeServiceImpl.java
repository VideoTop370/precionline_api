package com.workeache.precionline.api.demo.services;


import com.workeache.precionline.api.demo.persistence.entities.DataApiRee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Slf4j
@Service
public class ApiReeServiceImpl implements ApiReeService{

    @Autowired
    RestTemplate restTemplate;

    @Override
    public DataApiRee updatePrices(LocalDate dateFrom, LocalDate dateTo) {

        DataApiRee dataApiRee = new DataApiRee();

        HttpEntity<String> reeObjectHttpEntity = new HttpEntity<>("body",getHeaders());

        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        ResponseEntity<String> response =
                restTemplate.exchange(
                        "https://api.esios.ree.es/indicators/1001?start_date="+ dateFrom.format(formatDate) +"&end_date=" + dateTo.format(formatDate),
                        HttpMethod.GET,
                        reeObjectHttpEntity,
                        String.class);

        dataApiRee.setData(response.getBody());
        dataApiRee.setDateFile(dateFrom);

        return dataApiRee;

    }

    private HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        //TODO Token a fichero de propiedades
        headers.set("x-api-key", "608773a53bb93c375e5e5a1ea658f684df8b6777cec2c2d2c8e2602fbbabf4a0");

        return headers;
    }

}
