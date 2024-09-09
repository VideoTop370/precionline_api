package com.workeache.precionline.api.demo.batch;

import com.workeache.precionline.api.demo.persistence.entities.DataApiReeEntity;
import com.workeache.precionline.api.demo.services.ApiReeService;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReaderApiRee implements ItemReader<List<DataApiReeEntity>> {

    @Autowired
    private ApiReeService apiReeService;


    @Override
    public List<DataApiReeEntity> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        List<DataApiReeEntity> dataApiReeEntityList = new ArrayList<>();

        LocalDate actualDate = LocalDate.now().plusDays(1);
        LocalDate nextDayDate = actualDate.plusDays(1);
        //dataApiReeList.add(apiReeService.updatePrices(actualDate, nextDayDate));

        actualDate = actualDate.plusDays(1);
        nextDayDate = nextDayDate.plusDays(1);
        //dataApiReeList.add(apiReeService.updatePrices(actualDate, nextDayDate));

        return dataApiReeEntityList;
    }
}
