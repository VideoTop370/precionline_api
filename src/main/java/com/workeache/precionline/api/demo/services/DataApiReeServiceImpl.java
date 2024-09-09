package com.workeache.precionline.api.demo.services;

import com.workeache.precionline.api.demo.persistence.entities.DataApiReeEntity;
import com.workeache.precionline.api.demo.persistence.repositories.DataApiReeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class DataApiReeServiceImpl implements DataApiReeService{

    DataApiReeRepository dataApiReeRepository;

    @Autowired
    public DataApiReeServiceImpl(DataApiReeRepository dataApiReeRepository) {
        this.dataApiReeRepository = dataApiReeRepository;
    }

    @Override
    public DataApiReeEntity getActualDayPrices() {
        LocalDate localDate = LocalDate.now(ZoneId.of("Europe/Madrid"));

        return dataApiReeRepository.findByDateFile(localDate);
    }

    @Override
    public DataApiReeEntity getNextDayPrices() {
        LocalDate localDate = LocalDate.now(ZoneId.of("Europe/Madrid")).plusDays(1);
        return dataApiReeRepository.findByDateFile(localDate);
    }

    @Override
    public DataApiReeEntity getPricesByDate(LocalDate localDate) {
        return dataApiReeRepository.findByDateFile(localDate);
    }

    @Override
    public void save(DataApiReeEntity dataApiReeEntity) {

        if (!dataApiReeRepository.existsByDateFile(dataApiReeEntity.getDateFile())) {
            dataApiReeRepository.save(dataApiReeEntity);
        }else{
            //Si ya existe lo actualizo
            DataApiReeEntity dataApiReeEntityTemp = dataApiReeRepository.findByDateFile(dataApiReeEntity.getDateFile());
            dataApiReeEntityTemp.setData(dataApiReeEntity.getData());
            dataApiReeEntityTemp.setLastModifiedAuditDate(null);
            dataApiReeRepository.save(dataApiReeEntityTemp);
        }
    }
}
