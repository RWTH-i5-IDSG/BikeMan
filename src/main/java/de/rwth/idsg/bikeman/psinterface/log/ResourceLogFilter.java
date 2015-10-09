package de.rwth.idsg.bikeman.psinterface.log;

import de.rwth.idsg.bikeman.psinterface.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Taken from https://github.com/isrsal/spring-mvc-logger and modified
 *
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 09.10.2015
 */
@Slf4j
public class ResourceLogFilter extends OncePerRequestFilter {

    private static final String PREFIX_FORMAT = "[stationId=%s, requestId=%s] ";
    private static final String REQUEST_PREFIX = "Request: ";
    private static final String RESPONSE_PREFIX = "Response: ";

    private AtomicInteger id = new AtomicInteger(0);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        if (log.isDebugEnabled()) {
            String prefix = String.format(PREFIX_FORMAT, Utils.getFrom(request), id.incrementAndGet());

            request = new RequestWrapper(prefix, request);
            response = new ResponseWrapper(prefix, response);
        }

        try {
            filterChain.doFilter(request, response);
            //response.flushBuffer();
        } finally {
            if (log.isDebugEnabled()) {
                logRequest(request);
                logResponse(response);
            }
        }
    }

    private void logRequest(final HttpServletRequest request) {
        if (request instanceof RequestWrapper) {
            RequestWrapper wrap = (RequestWrapper) request;
            StringBuilder msg = new StringBuilder();

            msg.append(wrap.getPrefix())
               .append(REQUEST_PREFIX)
               .append("uri=")
               .append(request.getRequestURI());

            try {
                msg.append("; payload=")
                   .append(new String(wrap.toByteArray(), wrap.getCharacterEncoding()));
            } catch (UnsupportedEncodingException e) {
                log.warn("Failed to parse request payload", e);
            }

            log.debug(msg.toString());
        }
    }

    private void logResponse(final HttpServletResponse response) {
        if (response instanceof ResponseWrapper) {
            ResponseWrapper wrap = (ResponseWrapper) response;
            StringBuilder msg = new StringBuilder();

            msg.append(wrap.getPrefix())
               .append(RESPONSE_PREFIX)
               .append("statusCode=")
               .append(response.getStatus());

            try {
                msg.append("; payload=")
                   .append(new String(wrap.toByteArray(), wrap.getCharacterEncoding()));
            } catch (UnsupportedEncodingException e) {
                log.warn("Failed to parse response payload", e);
            }

            log.debug(msg.toString());
        }
    }
}
