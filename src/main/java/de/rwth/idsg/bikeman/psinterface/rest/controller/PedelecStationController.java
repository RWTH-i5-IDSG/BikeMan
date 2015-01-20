package de.rwth.idsg.bikeman.psinterface.rest.controller;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.bikeman.psinterface.Utils;
import de.rwth.idsg.bikeman.psinterface.dto.request.BootNotificationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.CardActivationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.CustomerAuthorizeDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.AuthorizeConfirmationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.BootConfirmationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.HeartbeatDTO;
import de.rwth.idsg.bikeman.psinterface.service.PedelecStationService;
import de.rwth.idsg.bikeman.service.CardAccountService;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
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

    @Inject
    private CardAccountService cardAccountService;

    private static final String BASE_PATH_BOOTNOTIFICATION = "/boot";
    private static final String BASE_PATH_AUTHORIZE = "/authorize";
    private static final String BASE_PATH_HEARTBEAT = "/heartbeat";
    private static final String BASE_PATH_ACTIVATE_CARD = "/activate-card";

    @Timed
    @RequestMapping(value = BASE_PATH_BOOTNOTIFICATION, method = RequestMethod.POST)
    public BootConfirmationDTO bootNotification(@RequestBody BootNotificationDTO bootNotificationDTO,
                                                HttpServletRequest request, HttpServletResponse response) throws DatabaseException {
        log.debug("[From: {}] Received bootNotification", Utils.getFrom(request));
        return pedelecStationService.handleBootNotification(bootNotificationDTO, response);
    }

    @Timed
    @RequestMapping(value = BASE_PATH_AUTHORIZE, method = RequestMethod.POST)
    public AuthorizeConfirmationDTO authorize(@RequestBody CustomerAuthorizeDTO customerAuthorizeDTO,
                                              HttpServletRequest request) throws DatabaseException {
        String cardId = customerAuthorizeDTO.getCardId();
        log.info("[From: {}] Received authorization request for card id '{}'", Utils.getFrom(request), cardId);
        AuthorizeConfirmationDTO dto = pedelecStationService.handleAuthorize(customerAuthorizeDTO);
        log.info("User with card id '{}' is authorized", cardId);
        return dto;
    }

    @Timed
    @RequestMapping(value = BASE_PATH_HEARTBEAT, method = RequestMethod.GET)
    public HeartbeatDTO heartbeat(HttpServletRequest request) {
        log.debug("[From: {}] Received heartbeat", Utils.getFrom(request));

        HeartbeatDTO heartbeatDTO = new HeartbeatDTO();
        heartbeatDTO.setTimestamp(new Date().getTime());
        return heartbeatDTO;
    }

    @Timed
    @RequestMapping(value = BASE_PATH_ACTIVATE_CARD, method = RequestMethod.POST)
    public AuthorizeConfirmationDTO activateCard(@RequestBody CardActivationDTO cardActivationDTO, HttpServletRequest request, HttpServletResponse response) {
        log.info("[From: {}] Received activate card request for activation key'{}'", Utils.getFrom(request), cardActivationDTO.getActivationKey());

        return cardAccountService.activateCardAccount(cardActivationDTO, response);
    }
    
}
