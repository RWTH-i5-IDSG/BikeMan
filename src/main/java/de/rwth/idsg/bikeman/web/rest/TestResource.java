package de.rwth.idsg.bikeman.web.rest;

import de.rwth.idsg.bikeman.psinterface.dto.request.RemoteAuthorizeDTO;
import de.rwth.idsg.bikeman.psinterface.rest.client.StationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 24.07.2015
 */
@RestController
@RequestMapping(value = "/api", produces = MediaType.TEXT_PLAIN_VALUE)
@Slf4j
public class TestResource {

    @Autowired private StationClient stationClient;

    @RequestMapping(value = "/auth/{slotPosition}/{cardId}", method = RequestMethod.GET)
    public String auth(@PathVariable Integer slotPosition,
                       @PathVariable String cardId) {
        RemoteAuthorizeDTO dto = RemoteAuthorizeDTO
                .builder()
                .slotPosition(slotPosition)
                .cardId(cardId)
                .build();

        stationClient.authorizeRemote("http://10.10.1.101:8080", dto);
        return "OK";
    }

    @RequestMapping(value = "/cancel-auth/{slotPosition}", method = RequestMethod.GET)
    public String cancelAuth(@PathVariable Integer slotPosition) {

        stationClient.cancelAuthorize(slotPosition, "http://10.10.1.101:8080");
        return "OK";
    }
}
