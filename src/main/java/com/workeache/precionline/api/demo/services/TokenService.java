package com.workeache.precionline.api.demo.services;

import com.workeache.precionline.api.demo.persistence.entities.TokenEntity;

import java.util.List;

public interface TokenService {

    public List<TokenEntity> get10Tokens();
    public boolean findToken(String token);
}
