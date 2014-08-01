package de.rwth.idsg.velocity.psinterface.rest.controller;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.psinterface.dto.request.BootNotificationDTO;
import de.rwth.idsg.velocity.psinterface.dto.request.CustomerAuthorizeDTO;
import de.rwth.idsg.velocity.psinterface.dto.response.AuthorizeConfirmationDTO;
import de.rwth.idsg.velocity.psinterface.dto.response.BootConfirmationDTO;
import de.rwth.idsg.velocity.psinterface.dto.response.HeartbeatDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by swam on 31/07/14.
 */

@RestController
@RequestMapping("/psi")
@Slf4j
public class PedelecStationResource {

    @RequestMapping(value = "/boot",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public BootConfirmationDTO bootNotification(BootNotificationDTO bootNotificationDTO) {
        return null;
    }

    @RequestMapping(value = "/authorize",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public AuthorizeConfirmationDTO authorize(CustomerAuthorizeDTO customerAuthorizeDTO) {
        return null;
    }

    @RequestMapping(value = "/heartbeat",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public HeartbeatDTO heartbeat() {
        return null;
    }

}
