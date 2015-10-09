package de.rwth.idsg.bikeman.psinterface.log;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 09.10.2015
 */
@Slf4j
public class ClientLogInterceptor implements ClientHttpRequestInterceptor {

    private static final String PREFIX_FORMAT = "[stationUrl=%s, requestId=%s]";
    private AtomicInteger id = new AtomicInteger(0);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {

        String prefix = String.format(PREFIX_FORMAT, request.getURI().toString(), id.incrementAndGet());

        logRequest(prefix, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(prefix, response);
        return response;
    }

    private void logRequest(String prefix, byte[] body) throws IOException {
        String message = IOUtils.toString(body, StandardCharsets.UTF_8.name());
        log.debug("{} Request: payload={}", prefix, message);
    }

    private void logResponse(String prefix, ClientHttpResponse response) throws IOException {
        String message = IOUtils.toString(response.getBody(), StandardCharsets.UTF_8.name());
        log.debug("{} Response: statusCode={}; payload={}", prefix, response.getRawStatusCode(), message);
    }
}
