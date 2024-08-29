package com.workeache.precionline.api.demo.services;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.workeache.precionline.api.demo.exceptions.DataNotUpdateException;
import com.workeache.precionline.api.demo.persistence.entities.DataApiRee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${precionline.ree.indicator.1001}")
    private String INDICATOR_1001;

    @Value("${precionline.ree.token}")
    private String REE_TOKEN;

    @Override
    public DataApiRee updatePrices(LocalDate dateFrom, LocalDate dateTo) throws DataNotUpdateException {

        DataApiRee dataApiRee = new DataApiRee();

        HttpEntity<String> reeObjectHttpEntity = new HttpEntity<>("body",getHeaders());

        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        ResponseEntity<String> response =
                restTemplate.exchange(
                        INDICATOR_1001 + "?start_date="+ dateFrom.format(formatDate) +"&end_date=" + dateTo.format(formatDate),
                        HttpMethod.GET,
                        reeObjectHttpEntity,
                        String.class);

        JsonObject jsonObject = new Gson().fromJson(response.getBody(), JsonObject.class);



        if (jsonObject.get("indicator").getAsJsonObject().get("values_updated_at").isJsonNull()){
            dataApiRee.setData(response.getBody());
            dataApiRee.setDateFile(dateFrom);
            throw new DataNotUpdateException("Datos de precios no disponibles en este momento.");
        }

        dataApiRee.setData(response.getBody());
        dataApiRee.setDateFile(dateFrom);

        return dataApiRee;

    }

    private HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", REE_TOKEN);

        return headers;
    }

}
