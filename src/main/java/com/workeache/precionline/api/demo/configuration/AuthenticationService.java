package com.workeache.precionline.api.demo.configuration;

import com.workeache.precionline.api.demo.services.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";
    private static final String APP_TYPE_HEADER_NAME = "APP-TYPE";
    private static final String AUTH_TOKEN_ANDROID = "EstoyC0mp3t4menteAferminado!";

    TokenService tokenService;

    @Autowired
    public AuthenticationService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        String appType = request.getHeader(APP_TYPE_HEADER_NAME);

        if (apiKey==null || appType==null){
            throw new BadCredentialsException("Invalid API Key");
        }

        switch (appType) {
            case "0":
                if (apiKey == null || !tokenService.findToken(apiKey)) {
                    throw new BadCredentialsException("Invalid API Key");
                }
                break;
            case "1":
                if (apiKey == null || !apiKey.equals(AUTH_TOKEN_ANDROID)) {
                    throw new BadCredentialsException("Invalid API Key");
                }
                break;
            default:
                throw new BadCredentialsException("Invalid API Key");
        }


        return new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }


}
