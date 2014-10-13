package de.rwth.idsg.bikeman.psinterface;

import de.rwth.idsg.bikeman.web.rest.dto.modify.ChangePedelecOperationStateDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.PedelecConfigurationDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.10.2014
 */
@Slf4j
@Component
public class PedelecClient {

    @Autowired private RestTemplate restTemplate;

    private static final String STATE_PATH = "/pedelecs/{pedelecManufacturerId}/state";
    private static final String CONFIG_PATH = "/pedelecs/{pedelecManufacturerId}/config";

    public boolean changeOperationState(String endpointAddress, String pedelecManufacturerId, ChangePedelecOperationStateDTO dto) {
        String uri = endpointAddress + STATE_PATH;
        ResponseEntity<String> response = restTemplate.postForEntity(uri, dto, String.class, pedelecManufacturerId);
        HttpStatus status = response.getStatusCode();

        if (status.equals(HttpStatus.ACCEPTED)) {
            return true;
        } else {
            // TODO
            return false;
        }
    }

    public PedelecConfigurationDTO getConfig(String endpointAddress, String pedelecManufacturerId) {
        String uri = endpointAddress + CONFIG_PATH;
        ResponseEntity<PedelecConfigurationDTO> response = restTemplate.getForEntity(uri,PedelecConfigurationDTO.class, pedelecManufacturerId);
        HttpStatus status = response.getStatusCode();

        if (status.equals(HttpStatus.OK)) {
            return response.getBody();
        } else {
            // TODO
            return null;
        }
    }

    public void changeConfig(String endpointAddress, String pedelecManufacturerId, PedelecConfigurationDTO dto) {
        String uri = endpointAddress + CONFIG_PATH;
        ResponseEntity<String> response = restTemplate.postForEntity(uri, dto, String.class, pedelecManufacturerId);
        HttpStatus status = response.getStatusCode();
        // TODO: Handle status codes
    }
}