package com.workeache.precionline.api.demo.persistence.repositories;

import com.workeache.precionline.api.demo.persistence.entities.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    public boolean existsByToken(String token);
}
