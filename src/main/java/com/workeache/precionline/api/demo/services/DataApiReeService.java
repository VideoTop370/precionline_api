package com.workeache.precionline.api.demo.services;

import com.workeache.precionline.api.demo.persistence.entities.DataApiReeEntity;

import java.time.LocalDate;


public interface DataApiReeService {
    public DataApiReeEntity getActualDayPrices();
    public DataApiReeEntity getNextDayPrices();
    public DataApiReeEntity getPricesByDate(LocalDate localDate);
    public void save(DataApiReeEntity dataApiReeEntity);
}
