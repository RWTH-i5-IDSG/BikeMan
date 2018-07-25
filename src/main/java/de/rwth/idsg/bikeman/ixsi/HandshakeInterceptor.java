package de.rwth.idsg.bikeman.ixsi;

import com.google.common.base.Strings;
import de.rwth.idsg.bikeman.config.IxsiConfiguration;
import de.rwth.idsg.bikeman.ixsi.repository.SystemValidator;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by max on 08/09/14.
 */
@Slf4j
@RequiredArgsConstructor
public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    private final SystemValidator systemValidator;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String systemId = getSystemID(request);
        if (systemId == null) {
            throw new DatabaseException("This ip address is not allowed for WebSocket communication");
        } else {
            // to be be used in future
            attributes.put(IxsiConfiguration.SYSTEM_ID_KEY, systemId);
            return super.beforeHandshake(request, response, wsHandler, attributes);
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
        log.trace("Handshake complete with {}", request.getRemoteAddress());
    }

    private String getSystemID(ServerHttpRequest request) {
        List<String> ipAddressList = getPossibleIpAddresses(request);

        for (String ip : ipAddressList) {
            try {
                return systemValidator.getSystemID(ip);
            } catch (DatabaseException e) {
                // not in db, continue with the next in the list
            }
        }
        return null;
    }

    private static List<String> getPossibleIpAddresses(ServerHttpRequest request) {
        List<String> ipAddressList = new ArrayList<>();

        getFromProxy(request).stream()
                             .filter(s -> !Strings.isNullOrEmpty(s))
                             .forEach(ipAddressList::add);

        ipAddressList.add(getFromRemote(request));
        ipAddressList.add(getFromContext());

        return ipAddressList;
    }

    private static List<String> getFromProxy(ServerHttpRequest request) {
        List<String> strings = request.getHeaders().get("X-FORWARDED-FOR");
        if (strings == null) {
            return Collections.emptyList();
        } else {
            return strings;
        }
    }

    private static String getFromRemote(ServerHttpRequest request) {
        InetSocketAddress inetSocketAddress = request.getRemoteAddress();
        return inetSocketAddress.getAddress().getHostAddress();
    }

    private static String getFromContext() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getRemoteAddr();
    }
}
