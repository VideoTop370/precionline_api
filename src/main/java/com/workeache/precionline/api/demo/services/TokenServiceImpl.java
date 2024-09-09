package com.workeache.precionline.api.demo.services;

import com.workeache.precionline.api.demo.persistence.entities.TokenEntity;
import com.workeache.precionline.api.demo.persistence.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenServiceImpl implements TokenService{

    TokenRepository tokenRepository;

    @Autowired
    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public List<TokenEntity> get10Tokens() {
        return tokenRepository.findAll();
    }

    @Override
    public boolean findToken(String token) {
        return tokenRepository.existsByToken(token);
    }
}
