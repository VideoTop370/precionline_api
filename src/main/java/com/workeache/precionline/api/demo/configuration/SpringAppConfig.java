package com.workeache.precionline.api.demo.configuration;

import com.workeache.precionline.api.demo.auditing.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class SpringAppConfig {
    @Bean
    public RestTemplate getresttemplate() {
        return new RestTemplate();
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

}
