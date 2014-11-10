package de.rwth.idsg.bikeman.ixsi;

import de.rwth.idsg.bikeman.ixsi.repository.SystemValidator;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * Created by max on 08/09/14.
 */
@Slf4j
public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    public static final String SYSTEM_ID_KEY = "systemId";

    private final SystemValidator systemValidator;

    public HandshakeInterceptor(SystemValidator systemValidator) {
        this.systemValidator = systemValidator;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        InetSocketAddress inetSocketAddress = request.getRemoteAddress();
        log.trace("Starting handshake with {}", inetSocketAddress);

        String ip = inetSocketAddress.getAddress().getHostAddress();
        try {
            String systemId = systemValidator.getSystemID(ip);
            // to be be used in future
            attributes.put(SYSTEM_ID_KEY, systemId);
            return super.beforeHandshake(request, response, wsHandler, attributes);

        } catch (DatabaseException e) {
            log.error("Exception occurred", e);
            throw e;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
        log.trace("Handshake complete with {}", request.getRemoteAddress());
    }
}
