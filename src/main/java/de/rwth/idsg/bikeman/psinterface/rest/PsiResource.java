package de.rwth.idsg.bikeman.psinterface.rest;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.Striped;
import de.rwth.idsg.bikeman.psinterface.Utils;
import de.rwth.idsg.bikeman.psinterface.dto.request.BootNotificationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.CardActivationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.CardActivationStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.ChargingStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.CustomerAuthorizeDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.FirmwareStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.LogsStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.PedelecStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StartTransactionDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StationStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StopTransactionDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.AuthorizeConfirmationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.BootConfirmationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.CardActivationResponseDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.HeartbeatDTO;
import de.rwth.idsg.bikeman.psinterface.exception.PsErrorCode;
import de.rwth.idsg.bikeman.psinterface.exception.PsException;
import de.rwth.idsg.bikeman.service.CardAccountService;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * Created by swam on 31/07/14.
 */

@RestController
@RequestMapping(value = "/psi", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class PsiResource {

    @Inject private PsiService psiService;
    @Inject private CardAccountService cardAccountService;

    private final Striped<Lock> stationLocks = Striped.lock(25);

    private static final String BOOT_NOTIFICATION_PATH = "/boot";
    private static final String AUTHORIZE_PATH = "/authorize";
    private static final String HEARTBEAT_PATH = "/heartbeat";
    private static final String ACTIVATE_CARD_PATH = "/activate-card";
    private static final String AVAIL_PEDELECS_PATH = "/available-pedelecs";

    private static final String CARD_ACTIVATION_STATUS_PATH = "/status/card-activation";
    private static final String STATION_STATUS_PATH = "/status/station";
    private static final String PEDELEC_STATUS_PATH = "/status/pedelec";
    private static final String CHARGING_STATUS_PATH = "/status/charging";
    private static final String FIRMWARE_STATUS_PATH = "/status/firmware";
    private static final String LOGS_STATUS_PATH = "/status/logs";

    private static final String TRANSACTION_START_PATH = "/transaction/start";
    private static final String TRANSACTION_STOP_PATH = "/transaction/stop";

    // -------------------------------------------------------------------------
    // Station
    // -------------------------------------------------------------------------

    @RequestMapping(value = BOOT_NOTIFICATION_PATH, method = RequestMethod.POST)
    public BootConfirmationDTO bootNotification(@RequestBody BootNotificationDTO bootNotificationDTO,
                                                HttpServletRequest request) throws DatabaseException {
        String stationId = Utils.getFrom(request);
        log.info("[From: {}] Received bootNotification {}", stationId, bootNotificationDTO);
        BootConfirmationDTO dto;

        Lock l = stationLocks.get(stationId);
        l.lock();
        try {
            dto = psiService.handleBootNotification(bootNotificationDTO);
        } finally {
            l.unlock();
        }

        log.debug("bootNotification returns {}", dto);
        return dto;
    }

    @RequestMapping(value = HEARTBEAT_PATH, method = RequestMethod.GET)
    public HeartbeatDTO heartbeat(HttpServletRequest request) {
        String stationId = Utils.getFrom(request);
        log.debug("[From: {}] Received heartbeat", stationId);

        HeartbeatDTO heartbeatDTO = new HeartbeatDTO();
        heartbeatDTO.setTimestamp(DateTime.now());
        return heartbeatDTO;
    }

    @RequestMapping(value = AVAIL_PEDELECS_PATH, method = RequestMethod.GET)
    public List<String> getAvailablePedelecs(@RequestParam(value = "cardId", required = false) String cardId,
                                             HttpServletRequest request) throws DatabaseException {
        String stationId = Utils.getFrom(request);
        log.info("[From: {}] Received getAvailablePedelecs for cardId '{}'", stationId, cardId);
        List<String> list;

        Lock l = stationLocks.get(stationId);
        l.lock();
        try {
            list = psiService.getAvailablePedelecs(stationId, cardId);
        } finally {
            l.unlock();
        }

        log.debug("getAvailablePedelecs returns {}", list);
        return list;
    }

    // -------------------------------------------------------------------------
    // User
    // -------------------------------------------------------------------------

    @RequestMapping(value = ACTIVATE_CARD_PATH, method = RequestMethod.POST)
    public CardActivationResponseDTO activateCard(@RequestBody CardActivationDTO cardActivationDTO,
                                                  HttpServletRequest request, HttpServletResponse response) {
        String stationId = Utils.getFrom(request);
        log.info("[From: {}] Received activateCard {}", stationId, cardActivationDTO);
        Optional<CardActivationResponseDTO> optional;

        Lock l = stationLocks.get(stationId);
        l.lock();
        try {
            optional = cardAccountService.activateCardAccount(cardActivationDTO);
        } finally {
            l.unlock();
        }

        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new PsException("Credentials are not accepted", PsErrorCode.CONSTRAINT_FAILED);
        }
    }

    @RequestMapping(value = AUTHORIZE_PATH, method = RequestMethod.POST)
    public AuthorizeConfirmationDTO authorize(@RequestBody CustomerAuthorizeDTO customerAuthorizeDTO,
                                              HttpServletRequest request) throws DatabaseException {
        String stationId = Utils.getFrom(request);
        log.info("[From: {}] Received authorize {}", stationId, customerAuthorizeDTO);
        AuthorizeConfirmationDTO dto;

        Lock l = stationLocks.get(stationId);
        l.lock();
        try {
            dto = psiService.handleAuthorize(customerAuthorizeDTO);
        } finally {
            l.unlock();
        }

        log.debug("authorize returns {}", dto);
        return dto;
    }

    // -------------------------------------------------------------------------
    // Transaction
    // -------------------------------------------------------------------------

    @RequestMapping(value = TRANSACTION_START_PATH, method = RequestMethod.POST)
    public void startTransaction(@RequestBody StartTransactionDTO startTransactionDTO,
                                 HttpServletRequest request) throws DatabaseException {
        String stationId = Utils.getFrom(request);
        log.info("[From: {}] Received startTransaction: {}", stationId, startTransactionDTO);

        Lock l = stationLocks.get(stationId);
        l.lock();
        try {
            psiService.handleStartTransaction(startTransactionDTO);
        } finally {
            l.unlock();
        }
    }

    @RequestMapping(value = TRANSACTION_STOP_PATH, method = RequestMethod.POST)
    public void stopTransaction(@RequestBody StopTransactionDTO stopTransactionDTO,
                                HttpServletRequest request) throws DatabaseException {
        String stationId = Utils.getFrom(request);
        log.info("[From: {}] Received stopTransaction: {}", stationId, stopTransactionDTO);

        Lock l = stationLocks.get(stationId);
        l.lock();
        try {
            psiService.handleStopTransaction(stopTransactionDTO);
        } finally {
            l.unlock();
        }
    }

    // -------------------------------------------------------------------------
    // Status
    // -------------------------------------------------------------------------

    @RequestMapping(value = CARD_ACTIVATION_STATUS_PATH, method = RequestMethod.POST)
    public void stationCardActivationNotification(@RequestBody CardActivationStatusDTO dto,
                                                  HttpServletRequest request) {
        String stationId = Utils.getFrom(request);
        log.info("[From: {}] Received cardActivationNotification: {}", stationId, dto);

        Lock l = stationLocks.get(stationId);
        l.lock();
        try {
            cardAccountService.setCardOperative(dto);
        } finally {
            l.unlock();
        }
    }

    @RequestMapping(value = STATION_STATUS_PATH, method = RequestMethod.POST)
    public void stationStatusNotification(@RequestBody StationStatusDTO stationStatusDTO,
                                          HttpServletRequest request) {
        String stationId = Utils.getFrom(request);
        log.info("[From: {}] Received stationStatusNotification: {}", stationId, stationStatusDTO);

        Lock l = stationLocks.get(stationId);
        l.lock();
        try {
            psiService.handleStationStatusNotification(stationStatusDTO);
        } finally {
            l.unlock();
        }
    }

    @RequestMapping(value = PEDELEC_STATUS_PATH, method = RequestMethod.POST)
    public void pedelecStatusNotification(@RequestBody PedelecStatusDTO pedelecStatusDTO,
                                          HttpServletRequest request) {
        String stationId = Utils.getFrom(request);
        log.info("[From: {}] Received pedelecStatusNotification: {}", stationId, pedelecStatusDTO);

        Lock l = stationLocks.get(stationId);
        l.lock();
        try {
            psiService.handlePedelecStatusNotification(pedelecStatusDTO);
        } finally {
            l.unlock();
        }
    }

    @RequestMapping(value = CHARGING_STATUS_PATH, method = RequestMethod.POST)
    public void chargingStatusNotification(@Valid @RequestBody List<ChargingStatusDTO> chargingStatusDTOs,
                                           HttpServletRequest request) {
        String stationId = Utils.getFrom(request);
        log.info("[From: {}] Received chargingStatusNotification: {}", stationId, chargingStatusDTOs);

        Lock l = stationLocks.get(stationId);
        l.lock();
        try {
            psiService.handleChargingStatusNotification(chargingStatusDTOs);
        } finally {
            l.unlock();
        }
    }

    @RequestMapping(value = FIRMWARE_STATUS_PATH, method = RequestMethod.POST)
    public void firmwareStatusNotification(@RequestBody FirmwareStatusDTO firmwareStatusDTO,
                                           HttpServletRequest request) {
        String stationId = Utils.getFrom(request);
        log.info("[From: {}] Received firmwareStatusNotification: {}", stationId, firmwareStatusDTO);
        // TODO
    }

    @RequestMapping(value = LOGS_STATUS_PATH, method = RequestMethod.POST)
    public void logsStatusNotification(@RequestBody LogsStatusDTO logsStatusDTO,
                                       HttpServletRequest request) {
        String stationId = Utils.getFrom(request);
        log.info("[From: {}] Received logsStatusNotification: {}", stationId, logsStatusDTO);
        // TODO
    }

}
