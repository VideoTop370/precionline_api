package com.workeache.precionline.api.demo.persistence.repositories;

import com.workeache.precionline.api.demo.persistence.entities.DataApiReeEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

public interface DataApiReeRepository extends CrudRepository<DataApiReeEntity, String> {
    public boolean existsByDateFile(LocalDate dateFile);

    public DataApiReeEntity findByDateFile(LocalDate dateFile);
}
