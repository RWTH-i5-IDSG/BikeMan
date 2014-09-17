package de.rwth.idsg.bikeman.psinterface.rest.controller;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.bikeman.psinterface.Utils;
import de.rwth.idsg.bikeman.psinterface.dto.request.ChargingStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.FirmwareStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.LogsStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.PedelecStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StationStatusDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by swam on 31/07/14.
 */

@RestController
@RequestMapping(value = "/psi/status", produces = "application/json")
@Slf4j
public class StatusController {

    private static final String BASE_PATH_STATION = "/station";
    private static final String BASE_PATH_PEDELEC = "/pedelec";
    private static final String BASE_PATH_CHARGING = "/charging";
    private static final String BASE_PATH_FIRMWARE = "/firmware";
    private static final String BASE_PATH_LOGS = "/logs";

    @Timed
    @RequestMapping(value = BASE_PATH_STATION, method = RequestMethod.POST)
    public void stationStatusNotification(@RequestBody StationStatusDTO stationStatusDTO, HttpServletRequest request) {
        log.debug("[From: {}] Received stationStatusNotification: {}", Utils.getFrom(request), stationStatusDTO);
        // TODO
    }

    @Timed
    @RequestMapping(value = BASE_PATH_PEDELEC, method = RequestMethod.POST)
    public void pedelecStatusNotification(@RequestBody PedelecStatusDTO pedelecStatusDTO, HttpServletRequest request) {
        log.debug("[From: {}] Received pedelecStatusNotification: {}", Utils.getFrom(request), pedelecStatusDTO);
        // TODO
    }

    @Timed
    @RequestMapping(value = BASE_PATH_CHARGING, method = RequestMethod.POST)
    public void chargingStatusNotification(@RequestBody List<ChargingStatusDTO> chargingStatusDTOs, HttpServletRequest request) {
        log.debug("[From: {}] Received chargingStatusNotification: {}", Utils.getFrom(request), chargingStatusDTOs);
        // TODO
    }

    @Timed
    @RequestMapping(value = BASE_PATH_FIRMWARE, method = RequestMethod.POST)
    public void firmwareStatusNotification(@RequestBody FirmwareStatusDTO firmwareStatusDTO, HttpServletRequest request) {
        log.debug("[From: {}] Received firmwareStatusNotification: {}", Utils.getFrom(request), firmwareStatusDTO);
        // TODO
    }

    @Timed
    @RequestMapping(value = BASE_PATH_LOGS, method = RequestMethod.POST)
    public void logsStatusNotification(@RequestBody LogsStatusDTO logsStatusDTO, HttpServletRequest request) {
        log.debug("[From: {}] Received logsStatusNotification: {}", Utils.getFrom(request), logsStatusDTO);
        // TODO
    }

}
