package de.rwth.idsg.bikeman.psinterface.rest.client;

import de.rwth.idsg.bikeman.psinterface.dto.request.CancelReservationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.RemoteAuthorizeDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.ReserveNowDTO;
import de.rwth.idsg.bikeman.psinterface.exception.PsExceptionBuilder;
import de.rwth.idsg.bikeman.web.rest.dto.modify.ChangeStationOperationStateDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.StationConfigurationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 09.10.2014
 */
@Slf4j
@Component
public class StationClient {

    @Autowired private RestTemplate restTemplate;
    @Autowired private PsExceptionBuilder psExceptionBuilder;

    private static final String STATE_PATH = "/state";
    private static final String CONFIG_PATH = "/config";
    private static final String REBOOT_PATH = "/reboot";
    private static final String AUTHORIZE_PATH = "/authorize/remote";
    private static final String RESERVE_NOW_PATH = "/reserve-now";
    private static final String CANCEL_RESERVATION_PATH = "/cancel-reservation";
    private static final String UNLOCK_SLOT_PATH = "/unlock/{slotPosition}";
    private static final String CANCEL_AUTHORIZE_PATH = "/authorize/cancel/{slotPosition}";

    public void changeOperationState(String endpointAddress, ChangeStationOperationStateDTO dto) {
        String uri = endpointAddress + STATE_PATH;
        try {
//            restTemplate.postForEntity(uri, dto, String.class);

        } catch (HttpStatusCodeException e) {
            throw psExceptionBuilder.build(e.getResponseBodyAsString());
        }
    }

    public StationConfigurationDTO getConfig(String endpointAddress) {
        String uri = endpointAddress + CONFIG_PATH;
        try {
            return restTemplate.getForEntity(uri, StationConfigurationDTO.class).getBody();

        } catch (HttpStatusCodeException e) {
            throw psExceptionBuilder.build(e.getResponseBodyAsString());
        }
    }

    public void changeConfig(String endpointAddress, StationConfigurationDTO dto) {
        String uri = endpointAddress + CONFIG_PATH;
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(uri, dto, String.class);
        } catch (HttpStatusCodeException e) {
            throw psExceptionBuilder.build(e.getResponseBodyAsString());
        }
    }

    public void reboot(String endpointAddress) {
        String uri = endpointAddress + REBOOT_PATH;
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(uri, null, String.class);
        } catch (HttpStatusCodeException e) {
            throw psExceptionBuilder.build(e.getResponseBodyAsString());
        }
    }

    public void authorizeRemote(String endpointAddress, RemoteAuthorizeDTO dto) {
        String uri = endpointAddress + AUTHORIZE_PATH;
        try {
            restTemplate.postForEntity(uri, dto, String.class);
        } catch (HttpStatusCodeException e) {
            throw psExceptionBuilder.build(e.getResponseBodyAsString());
        }
    }

    public void reserveNow(ReserveNowDTO dto, String endpointAddress) {
        String uri = endpointAddress + RESERVE_NOW_PATH;
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(uri, dto, String.class);
        } catch (HttpStatusCodeException e) {
            throw psExceptionBuilder.build(e.getResponseBodyAsString());
        }
    }

    public void cancelReservation(CancelReservationDTO dto, String endpointAddress) {
        String uri = endpointAddress + CANCEL_RESERVATION_PATH;
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(uri, dto, String.class);
        } catch (HttpStatusCodeException e) {
            throw psExceptionBuilder.build(e.getResponseBodyAsString());
        }
    }

    public void cancelAuthorize(Integer slotPosition, String endpointAddress) {
        String uri = endpointAddress + CANCEL_AUTHORIZE_PATH;
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(uri, null, String.class, slotPosition);
        } catch (HttpStatusCodeException e) {
            throw psExceptionBuilder.build(e.getResponseBodyAsString());
        }
    }

    public void unlockSlot(Integer slotPosition, String endpointAddress) {
        String uri = endpointAddress + UNLOCK_SLOT_PATH;
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(uri, null, String.class, slotPosition);
        } catch (HttpStatusCodeException e) {
            throw psExceptionBuilder.build(e.getResponseBodyAsString());
        }
    }
}
