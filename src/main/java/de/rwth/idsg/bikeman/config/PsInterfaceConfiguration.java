package de.rwth.idsg.bikeman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.10.2014
 */
@Configuration
public class PsInterfaceConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}