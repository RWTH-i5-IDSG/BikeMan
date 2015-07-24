package de.rwth.idsg.bikeman.psinterface.rest.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 24.07.2015
 */
@Component
public class HeaderInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        request.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        request.getHeaders().add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        return execution.execute(request, body);
    }
}
