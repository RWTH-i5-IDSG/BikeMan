package de.rwth.idsg.bikeman.config;

import de.rwth.idsg.bikeman.ApplicationConfig;
import de.rwth.idsg.bikeman.ixsi.HandshakeInterceptor;
import de.rwth.idsg.bikeman.ixsi.WebSocketEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * Created by max on 23/09/14.
 */
@EnableWebSocket
@Configuration
@Slf4j
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Autowired private WebSocketEndpoint webSocketEndpoint;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketEndpoint, ApplicationConfig.IXSI.WS_ENDPOINT)
                .addInterceptors(new HandshakeInterceptor());
    }

    @Bean()
    public JAXBContext jaxbContext() throws JAXBException {
        // is thread-safe
        return JAXBContext.newInstance(ApplicationConfig.IXSI.JAXB_CONTEXT_PATH);
    }
}