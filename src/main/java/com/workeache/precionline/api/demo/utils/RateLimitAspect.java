package com.workeache.precionline.api.demo.utils;

import com.workeache.precionline.api.demo.controller.PricesController;
import com.workeache.precionline.api.demo.exceptions.RateLimitException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class RateLimitAspect {
    public static final String ERROR_MESSAGE = "To many request at endpoint %s from IP %s! Please try again after %d milliseconds!";

    private final Logger logger = LoggerFactory.getLogger(PricesController.class);


    private final ConcurrentHashMap<String, List<Long>> requestCounts = new ConcurrentHashMap<>();

    @Value("${precionline.rate.limit}")
    private int rateLimit;

    @Value("${precionline.rate.durationinms}")
    private long rateDuration;

    @Value("${precionline.server.ip}")
    private String serverIp;

    /**
     * Executed by each call of a method annotated with {@link WithRateLimitProtection} which should be an HTTP endpoint.
     * Counts calls per remote address. Calls older than {@link #rateDuration} milliseconds will be forgotten. If there have
     * been more than {@link #rateLimit} calls within {@link #rateDuration} milliseconds from a remote address, a {@link RateLimitException}
     * will be thrown.
     * @throws RateLimitException iff rate limit for a given remote address has been exceeded
     */
    @Before("@annotation(com.workeache.precionline.api.demo.utils.annotations.WithRateLimitProtection)")
    public void rateLimit() {

        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        final String key = requestAttributes.getRequest().getRemoteAddr();

        //Si la llamada es desde el servidor Web no se realiza validación
        if (!isRequestFromWebServer(key)){
            final long currentTime = System.currentTimeMillis();
            requestCounts.putIfAbsent(key, new ArrayList<>());
            requestCounts.get(key).add(currentTime);
            cleanUpRequestCounts(currentTime);
            if (requestCounts.get(key).size() > rateLimit) {
                throw new RateLimitException(String.format(ERROR_MESSAGE, requestAttributes.getRequest().getRequestURI(), key, rateDuration));
            }
        }

    }

    private void cleanUpRequestCounts(final long currentTime) {
        requestCounts.values().forEach(l -> {
            l.removeIf(t -> timeIsTooOld(currentTime, t));
        });
    }

    private boolean timeIsTooOld(final long currentTime, final long timeToCheck) {
        return currentTime - timeToCheck > rateDuration;
    }

    private boolean isRequestFromWebServer(String clientIp) {

        logger.info(String.format("Ip origen: %s - Filtro Ip: %s", clientIp, serverIp));
        return serverIp.substring(0, 6).equals(clientIp.substring(0,6));
    }
}
