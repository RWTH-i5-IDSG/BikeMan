package de.rwth.idsg.velocity.psinterface.rest.controller;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.psinterface.dto.request.*;
import de.rwth.idsg.velocity.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public void stationStatusNotification(StationStatusDTO stationStatusDTO) {

    }

    @RequestMapping(value = BASE_PATH_PEDELEC,
            method = RequestMethod.POST)
    @Timed
    public void pedelecStatusNotification(PedelecStatusDTO pedelecStatusDTO) {

    }

    @RequestMapping(value = BASE_PATH_CHARGING,
            method = RequestMethod.POST)
    @Timed
    public void chargingStatusNotification(ChargingStatusDTO chargingStatusDTO) {

    }

    @RequestMapping(value = BASE_PATH_FIRMWARE,
            method = RequestMethod.POST)
    @Timed
    public void firmwareStatusNotification(FirmwareStatusDTO firmwareStatusDTO) {

    }

    @RequestMapping(value = BASE_PATH_LOGS,
            method = RequestMethod.POST)
    @Timed
    public void logsStatusNotification(LogsStatusDTO logsStatusDTO) {

    }

}
