package de.rwth.idsg.velocity.psinterface.rest.controller;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.psinterface.dto.request.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by swam on 31/07/14.
 */

@RestController
@RequestMapping("/psi/status")
@Slf4j
public class StatusResource {

    @RequestMapping(value = "/station",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public void stationStatusNotification(StationStatusDTO stationStatusDTO) {

    }

    @RequestMapping(value = "/pedelec",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public void pedelecStatusNotification(PedelecStatusDTO pedelecStatusDTO) {

    }

    @RequestMapping(value = "/charging",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public void chargingStatusNotification(ChargingStatusDTO chargingStatusDTO) {

    }

    @RequestMapping(value = "/firmware",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public void firmwareStatusNotification(FirmwareStatusDTO firmwareStatusDTO) {

    }

    @RequestMapping(value = "/logs",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public void logsStatusNotification(LogsStatusDTO logsStatusDTO) {

    }

}
