package com.workeache.precionline.api.demo.services;

import com.workeache.precionline.api.demo.exceptions.DataNotUpdateException;
import com.workeache.precionline.api.demo.persistence.entities.DataApiRee;

import java.time.LocalDate;

public interface ApiReeService {
    DataApiRee updatePrices(LocalDate dateFrom, LocalDate dateTo) throws DataNotUpdateException;
}
