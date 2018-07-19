package de.rwth.idsg.bikeman.psinterface.rest.client;

import com.google.common.base.Strings;
import de.rwth.idsg.bikeman.psinterface.exception.PsExceptionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 19.07.2018
 */
public abstract class AbstractClient {

    @Autowired protected RestTemplate restTemplate;
    @Autowired protected PsExceptionBuilder psExceptionBuilder;

    protected void checkIfValid(String endpointAddress) {
        if (Strings.isNullOrEmpty(endpointAddress)) {
            throw psExceptionBuilder.buildFromMsg("Endpoint address of the station is not set");
        }
    }
}
