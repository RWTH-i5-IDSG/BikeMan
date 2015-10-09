package de.rwth.idsg.bikeman.psinterface.log;

import de.rwth.idsg.bikeman.psinterface.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 09.10.2015
 */
@Slf4j
public class ResourceLogFilter extends OncePerRequestFilter {

    private AtomicInteger id = new AtomicInteger(0);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String from = Utils.getFrom(request);
        int requestId = id.incrementAndGet();

        logRequest(from, requestId, request);
        HttpServletResponseCopier responseCopier = new HttpServletResponseCopier(response);
        try {
            filterChain.doFilter(request, responseCopier);
            responseCopier.flushBuffer();
        } finally {
            logResponse(from, requestId, responseCopier);
        }
    }

    private void logRequest(String from, int requestId, ServletRequest request) throws IOException {
        String message = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        log.debug("[stationId={}, requestId={}] Received request: {}", from, requestId, message);
    }

    private void logResponse(String from, int requestId, HttpServletResponseCopier responseCopier) throws IOException {
        String message = IOUtils.toString(responseCopier.getCopy(), responseCopier.getCharacterEncoding());
        log.debug("[stationId={}, requestId={}] Sent response: {}", from, requestId, message);
    }
}
