package de.rwth.idsg.bikeman.config;

import de.rwth.idsg.bikeman.ixsi.HandshakeInterceptor;
import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.endpoint.WebSocketEndpoint;
import de.rwth.idsg.bikeman.ixsi.repository.SystemValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import xjc.schema.ixsi.IxsiMessageType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

/**
 * Created by max on 23/09/14.
 */
@EnableWebSocket
@Configuration
@Slf4j
public class IxsiConfiguration implements WebSocketConfigurer {

    public static final String WS_ENDPOINT  = "/ws";
    public static final String SYSTEM_ID_KEY = "systemId";
    
    @Autowired private WebSocketEndpoint webSocketEndpoint;
    @Autowired private SystemValidator systemValidator;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketEndpoint, WS_ENDPOINT).setAllowedOrigins("*")
                .addInterceptors(new HandshakeInterceptor(systemValidator));
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(IXSIConstants.MAX_TEXT_MSG_SIZE);
        return container;
    }

    @Bean
    public JAXBContext jaxbContext() throws JAXBException {
        // is thread-safe
        return JAXBContext.newInstance(IxsiMessageType.class);
    }

    @Bean
    public DatatypeFactory datatypeFactory() throws DatatypeConfigurationException {
        // This is expensive to init
        return DatatypeFactory.newInstance();
    }
}
