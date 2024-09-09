package com.workeache.precionline.api.demo.services;

import com.workeache.precionline.api.demo.exceptions.DataNotUpdateException;
import com.workeache.precionline.api.demo.persistence.entities.DataApiReeEntity;

import java.time.LocalDate;

public interface ApiReeService {
    DataApiReeEntity updatePrices(LocalDate dateFrom, LocalDate dateTo, boolean validate) throws DataNotUpdateException;
}
