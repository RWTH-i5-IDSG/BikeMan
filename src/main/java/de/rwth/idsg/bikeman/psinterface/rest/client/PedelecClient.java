package de.rwth.idsg.bikeman.psinterface.rest.client;

import de.rwth.idsg.bikeman.psinterface.exception.PsExceptionBuilder;
import de.rwth.idsg.bikeman.web.rest.dto.modify.ChangePedelecOperationStateDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.PedelecConfigurationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.10.2014
 */
@Slf4j
@Component
public class PedelecClient {

    @Autowired private RestTemplate restTemplate;
    @Autowired private PsExceptionBuilder psExceptionBuilder;

    private static final String STATE_PATH = "/pedelecs/{pedelecManufacturerId}/state";
    private static final String CONFIG_PATH = "/pedelecs/{pedelecManufacturerId}/config";

    public void changeOperationState(String endpointAddress, String pedelecManufacturerId, ChangePedelecOperationStateDTO dto) {
        String uri = endpointAddress + STATE_PATH;
        try {
            restTemplate.postForEntity(uri, dto, String.class, pedelecManufacturerId);

        } catch (HttpStatusCodeException e) {
            throw psExceptionBuilder.build(e.getResponseBodyAsString());
        }
    }

    public PedelecConfigurationDTO getConfig(String endpointAddress, String pedelecManufacturerId) {
        String uri = endpointAddress + CONFIG_PATH;
        try {
            return restTemplate.getForEntity(uri, PedelecConfigurationDTO.class, pedelecManufacturerId).getBody();

        } catch (HttpStatusCodeException e) {
            throw psExceptionBuilder.build(e.getResponseBodyAsString());
        }
    }

    public void changeConfig(String endpointAddress, String pedelecManufacturerId, PedelecConfigurationDTO dto) {
        String uri = endpointAddress + CONFIG_PATH;
        try {
            restTemplate.postForEntity(uri, dto, String.class, pedelecManufacturerId);

        } catch (HttpStatusCodeException e) {
            throw psExceptionBuilder.build(e.getResponseBodyAsString());
        }
    }
}
