package de.rwth.idsg.bikeman.config;

import de.rwth.idsg.bikeman.psinterface.log.ClientLogInterceptor;
import de.rwth.idsg.bikeman.psinterface.log.ResourceLogFilter;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.10.2014
 */
@Configuration
public class PsInterfaceConfiguration {

    @Bean
    public FilterRegistrationBean resourceLogFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new ResourceLogFilter());
        registration.addUrlPatterns("/psi/**");
        return registration;
    }

    @Bean
    public RestTemplate restTemplate() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new ClientLogInterceptor());

        RestTemplate restTemplate = new RestTemplate(
                new BufferingClientHttpRequestFactory(
                        new SimpleClientHttpRequestFactory()));
        
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
}