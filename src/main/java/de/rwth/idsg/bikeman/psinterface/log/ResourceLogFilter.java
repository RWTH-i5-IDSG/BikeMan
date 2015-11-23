package de.rwth.idsg.bikeman.psinterface.log;

import com.google.common.base.Joiner;
import de.rwth.idsg.bikeman.psinterface.Utils;
import de.rwth.idsg.bikeman.psinterface.exception.PsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
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
    private static final Joiner.MapJoiner joiner = Joiner.on(", ").withKeyValueSeparator("=");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        if (log.isDebugEnabled()) {
            String from = null;
            boolean stationHeaderMissing = false;
            try {
                from = Utils.getFrom(request);
            } catch (PsException e) {
                stationHeaderMissing = true;
            }

            String prefix = String.format(PREFIX_FORMAT, from, id.incrementAndGet());

            request = new RequestWrapper(prefix, request, stationHeaderMissing);
            response = new ResponseWrapper(prefix, response);
            logRequest(request);
        }

        try {
            filterChain.doFilter(request, response);
            //response.flushBuffer();
        } finally {
            if (log.isDebugEnabled()) {
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

            if (wrap.isStationHeaderMissing()) {
                log.error("stationId header was missing. Headers=[{}]", joiner.join(getHeadersMap(request)));
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

    private Map<String, String> getHeadersMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String key = (String) names.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }
}
