package de.rwth.idsg.bikeman.psinterface.rest.controller;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.bikeman.psinterface.dto.request.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = BASE_PATH_STATION,
            method = RequestMethod.POST)
    @Timed
    public void stationStatusNotification(@RequestBody StationStatusDTO stationStatusDTO) {
        log.debug("Received StationStatusDTO: {}", stationStatusDTO);
        // TODO
    }

    @RequestMapping(value = BASE_PATH_PEDELEC,
            method = RequestMethod.POST)
    @Timed
    public void pedelecStatusNotification(@RequestBody PedelecStatusDTO pedelecStatusDTO) {
        log.debug("Received Pedelec Status: {}", pedelecStatusDTO);
        // TODO
    }

    @RequestMapping(value = BASE_PATH_CHARGING,
            method = RequestMethod.POST)
    @Timed
    public void chargingStatusNotification(@RequestBody List<ChargingStatusDTO> chargingStatusDTOs) {
        log.debug("Received Charging Status: {}", chargingStatusDTOs);
        // TODO
    }

    @RequestMapping(value = BASE_PATH_FIRMWARE,
            method = RequestMethod.POST)
    @Timed
    public void firmwareStatusNotification(@RequestBody FirmwareStatusDTO firmwareStatusDTO) {
        // TODO
    }

    @RequestMapping(value = BASE_PATH_LOGS,
            method = RequestMethod.POST)
    @Timed
    public void logsStatusNotification(@RequestBody LogsStatusDTO logsStatusDTO) {
        // TODO
    }

}
