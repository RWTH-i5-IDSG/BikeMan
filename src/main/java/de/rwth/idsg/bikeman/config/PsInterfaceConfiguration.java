package de.rwth.idsg.bikeman.config;

import de.rwth.idsg.bikeman.psinterface.rest.client.HeaderInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.10.2014
 */
@Configuration
public class PsInterfaceConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.getInterceptors().add(new HeaderInterceptor());
        return restTemplate;
    }
}