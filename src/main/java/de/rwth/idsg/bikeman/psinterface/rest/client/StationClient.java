package de.rwth.idsg.bikeman.psinterface.rest.client;

import de.rwth.idsg.bikeman.psinterface.dto.request.CancelReservationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.RemoteAuthorizeDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.ReserveNowDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.ChangeStationOperationStateDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.StationConfigurationDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 09.10.2014
 */
@Slf4j
@Component
public class StationClient {

    @Autowired
    private RestTemplate restTemplate;

    private static final String STATE_PATH = "/state";
    private static final String CONFIG_PATH = "/config";
    private static final String REBOOT_PATH = "/reboot";
    private static final String AUTHORIZE_PATH = "/authorize/remote";
    private static final String RESERVE_NOW_PATH = "/reserve-now";
    private static final String CANCEL_RESERVATION_PATH = "/cancel-reservation";
    private static final String UNLOCK_SLOT_PATH = "/unlock/{slotPosition}";

    public boolean changeOperationState(String endpointAddress, ChangeStationOperationStateDTO dto)
        throws DatabaseException, RestClientException {

        String uri = endpointAddress + STATE_PATH;
        ResponseEntity<String> response = restTemplate.postForEntity(uri, dto, String.class);
        HttpStatus status = response.getStatusCode();

        if (status.equals(HttpStatus.OK)) {
            return true;
        } else {
            // TODO
            return false;
        }
    }

    public StationConfigurationDTO getConfig(String endpointAddress) throws DatabaseException, RestClientException {
        String uri = endpointAddress + CONFIG_PATH;
        ResponseEntity<StationConfigurationDTO> response = restTemplate.getForEntity(uri, StationConfigurationDTO.class);
        HttpStatus status = response.getStatusCode();

        if (status.equals(HttpStatus.OK)) {
            return response.getBody();
        } else {
            // TODO
            return null;
        }
    }

    public void changeConfig(String endpointAddress, StationConfigurationDTO dto) throws RestClientException, DatabaseException {
        String uri = endpointAddress + CONFIG_PATH;
        ResponseEntity<String> response = restTemplate.postForEntity(uri, dto, String.class);
        HttpStatus status = response.getStatusCode();
        // TODO: Handle status codes
    }

    public void reboot(String endpointAddress) throws RestClientException, DatabaseException {
        String uri = endpointAddress + REBOOT_PATH;
        ResponseEntity<String> response = restTemplate.postForEntity(uri, null, String.class);
        HttpStatus status = response.getStatusCode();
        // TODO: Handle status codes
    }

    public void authorizeRemote(String endpointAddress, RemoteAuthorizeDTO dto) throws RestClientException, DatabaseException {
        String uri = endpointAddress + AUTHORIZE_PATH;
        ResponseEntity<String> response = restTemplate.postForEntity(uri, dto, String.class);
        HttpStatus status = response.getStatusCode();
        // TODO: Handle status codes
    }

    public void reserveNow(ReserveNowDTO dto, String endpointAddress) {
        String uri = endpointAddress + RESERVE_NOW_PATH;
        ResponseEntity<String> response = restTemplate.postForEntity(uri, dto, String.class);
        // TODO: Handle status codes
    }

    public void cancelReservation(CancelReservationDTO dto, String endpointAddress) {
        String uri = endpointAddress + CANCEL_RESERVATION_PATH;
        ResponseEntity<String> response = restTemplate.postForEntity(uri, dto, String.class);
        // TODO: Handle status codes
    }

    public void unlockSlot(Integer slotPosition, String endpointAddress) {
        String uri = endpointAddress + UNLOCK_SLOT_PATH;

//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Accept", "application/json");
//        HttpEntity entity = new HttpEntity(headers);
//        HttpEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class, slotPosition);

//        Map<String, String> params = new HashMap<String, String>();
//        params.put("slotPosition", String.valueOf(slotPosition));


        ResponseEntity<String> response = restTemplate.postForEntity(uri, null, String.class, slotPosition);
    }
}
