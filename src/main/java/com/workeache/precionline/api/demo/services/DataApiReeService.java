package com.workeache.precionline.api.demo.services;

import com.workeache.precionline.api.demo.persistence.entities.DataApiRee;

import java.time.LocalDate;


public interface DataApiReeService {
    public DataApiRee getActualDayPrices();
    public DataApiRee getNextDayPrices();
    public DataApiRee getPricesByDate(LocalDate localDate);
    public void save(DataApiRee dataApiRee);
}
