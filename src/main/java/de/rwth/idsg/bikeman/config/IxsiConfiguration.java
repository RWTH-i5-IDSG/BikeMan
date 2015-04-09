package de.rwth.idsg.bikeman.config;

import de.rwth.idsg.bikeman.ApplicationConfig;
import de.rwth.idsg.bikeman.ixsi.HandshakeInterceptor;
import de.rwth.idsg.bikeman.ixsi.WebSocketEndpoint;
import de.rwth.idsg.bikeman.ixsi.repository.SystemValidator;
import de.rwth.idsg.bikeman.ixsi.schema.IxsiMessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

/**
 * Created by max on 23/09/14.
 */
@EnableWebSocket
@Configuration
@ComponentScan("de.rwth.idsg.bikeman.ixsi")
@Slf4j
public class IxsiConfiguration implements WebSocketConfigurer {

    @Autowired
    private WebSocketEndpoint webSocketEndpoint;
    @Autowired
    private SystemValidator systemValidator;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketEndpoint, ApplicationConfig.IXSI.WS_ENDPOINT).setAllowedOrigins("*")
                .addInterceptors(new HandshakeInterceptor(systemValidator));
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
