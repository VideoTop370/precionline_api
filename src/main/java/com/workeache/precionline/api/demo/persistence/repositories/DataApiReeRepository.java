package com.workeache.precionline.api.demo.persistence.repositories;

import com.workeache.precionline.api.demo.persistence.entities.DataApiRee;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

public interface DataApiReeRepository extends CrudRepository<DataApiRee, String> {
    public boolean existsByDateFile(LocalDate dateFile);

    public DataApiRee findByDateFile(LocalDate dateFile);
}
