package de.rwth.idsg.bikeman.ixsi;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import de.rwth.idsg.bikeman.config.IxsiConfiguration;
import de.rwth.idsg.bikeman.ixsi.repository.SystemValidator;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by max on 08/09/14.
 */
@Slf4j
@RequiredArgsConstructor
public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    private static final String X_FORWARDED_HEADER = "X-FORWARDED-FOR";
    private static final Splitter COMMA_SPLITTER = Splitter.on(',').trimResults();

    private final SystemValidator systemValidator;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String systemId = getSystemID(request);
        // to be be used in future
        attributes.put(IxsiConfiguration.SYSTEM_ID_KEY, systemId);
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
        log.trace("Handshake complete with {}", request.getRemoteAddress());
    }

    private String getSystemID(ServerHttpRequest request) {
        Set<String> ipAddresses = getPossibleIpAddresses(request);
        log.info("Possible client ip addresses for this WebSocket request: {}", ipAddresses);

        for (String ip : ipAddresses) {
            try {
                return systemValidator.getSystemID(ip);
            } catch (DatabaseException e) {
                // not in db, continue with the next in the list
            }
        }

        // if we come thus far, we have to decline this WebSocket request
        throw new DatabaseException("Not allowed for WebSocket communication");
    }

    private static Set<String> getPossibleIpAddresses(ServerHttpRequest request) {
        List<String> ipAddressList = getFromProxy(request);
        ipAddressList.add(getSingle(request));

        return ipAddressList.stream()
                            .filter(s -> !Strings.isNullOrEmpty(s))
                            .collect(Collectors.toSet());
    }

    private static List<String> getFromProxy(ServerHttpRequest request) {
        return getHeaderValues(request.getHeaders().get(X_FORWARDED_HEADER));
    }

    /**
     * Since Spring uses many abstractions with different APIs, we try every possible extraction method there available.
     */
    private static String getSingle(ServerHttpRequest request) {
        String s = request.getRemoteAddress().getAddress().getHostAddress();

        if (s == null) {
            HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            s = req.getRemoteAddr();
        }

        if (s == null && request instanceof ServletServerHttpRequest) {
            HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
            s = req.getRemoteAddr();
        }

        return s;
    }

    /**
     * Because we have to be able to handle both cases:
     *
     * 1) Multiple headers with the same key but different values:
     *      someHeader = value1
     *      someHeader = value2
     *
     * 2) One header with a comma separated list of values:
     *      someHeader = value1,value2
     */
    private static List<String> getHeaderValues(Collection<String> col) {
        List<String> valueList = new ArrayList<>();
        if (col == null) {
            return valueList;
        }

        Enumeration<String> valueEnums = Collections.enumeration(col);
        while (valueEnums.hasMoreElements()) {
            COMMA_SPLITTER.split(valueEnums.nextElement())
                          .forEach(valueList::add);
        }
        return valueList;
    }
}
