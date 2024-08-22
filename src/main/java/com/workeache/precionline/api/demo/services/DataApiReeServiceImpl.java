package com.workeache.precionline.api.demo.services;

import com.workeache.precionline.api.demo.persistence.entities.DataApiRee;
import com.workeache.precionline.api.demo.persistence.repositories.DataApiReeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Service
public class DataApiReeServiceImpl implements DataApiReeService{

    DataApiReeRepository dataApiReeRepository;

    @Autowired
    public DataApiReeServiceImpl(DataApiReeRepository dataApiReeRepository) {
        this.dataApiReeRepository = dataApiReeRepository;
    }

    @Override
    public DataApiRee getActualDayPrices() {
        LocalDate localDate = LocalDate.now(ZoneId.of("Europe/Madrid"));

        return dataApiReeRepository.findByDateFile(localDate);
    }

    @Override
    public DataApiRee getNextDayPrices() {
        LocalDate localDate = LocalDate.now(ZoneId.of("Europe/Madrid")).plusDays(1);
        return dataApiReeRepository.findByDateFile(localDate);
    }

    @Override
    public DataApiRee getPricesByDate(LocalDate localDate) {
        return dataApiReeRepository.findByDateFile(localDate);
    }

    @Override
    public void save(DataApiRee dataApiRee) {

        if (!dataApiReeRepository.existsByDateFile(dataApiRee.getDateFile())) {
            dataApiReeRepository.save(dataApiRee);
        }else{
            //Si ya existe lo actualizo
            DataApiRee dataApiReeTemp = dataApiReeRepository.findByDateFile(dataApiRee.getDateFile());
            dataApiReeTemp.setData(dataApiRee.getData());
            dataApiReeTemp.setLastModifiedAuditDate(null);
            dataApiReeRepository.save(dataApiReeTemp);
        }
    }
}
