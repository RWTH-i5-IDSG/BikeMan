package de.rwth.idsg.velocity.psinterface.rest.controller;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.domain.Customer;
import de.rwth.idsg.velocity.psinterface.dto.request.BootNotificationDTO;
import de.rwth.idsg.velocity.psinterface.dto.request.CustomerAuthorizeDTO;
import de.rwth.idsg.velocity.psinterface.dto.response.AuthorizeConfirmationDTO;
import de.rwth.idsg.velocity.psinterface.dto.response.BootConfirmationDTO;
import de.rwth.idsg.velocity.psinterface.dto.response.HeartbeatDTO;
import de.rwth.idsg.velocity.psinterface.service.PedelecStationService;
import de.rwth.idsg.velocity.repository.CustomerRepository;
import de.rwth.idsg.velocity.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by swam on 31/07/14.
 */

@RestController
@RequestMapping(value = "/psi", produces = "application/json")
@Slf4j
public class PedelecStationController {

    @Inject
    private PedelecStationService pedelecStationService;

    private static final String BASE_PATH_BOOTNOTIFICATION = "/boot";
    private static final String BASE_PATH_AUTHORIZE = "/authorize";
    private static final String BASE_PATH_HEARTBEAT = "/heartbeat";

    @RequestMapping(value = BASE_PATH_BOOTNOTIFICATION,
                    method = RequestMethod.POST)
    @Timed
    public BootConfirmationDTO bootNotification(@RequestBody BootNotificationDTO bootNotificationDTO)
            throws DatabaseException {

        log.debug(bootNotificationDTO.toString());
        return pedelecStationService.handleBootNotification(bootNotificationDTO);
    }

    @RequestMapping(value = BASE_PATH_AUTHORIZE,
                    method = RequestMethod.POST)
    @Timed
    public AuthorizeConfirmationDTO authorize(@RequestBody CustomerAuthorizeDTO customerAuthorizeDTO)
            throws DatabaseException {

        return pedelecStationService.handleAuthorize(customerAuthorizeDTO);
    }

    @RequestMapping(value = BASE_PATH_HEARTBEAT,
                    method = RequestMethod.GET)
    @Timed
    public HeartbeatDTO heartbeat() {
        HeartbeatDTO heartbeatDTO = new HeartbeatDTO();
        heartbeatDTO.setTimestamp(new Date().getTime());
        return heartbeatDTO;
    }

}
