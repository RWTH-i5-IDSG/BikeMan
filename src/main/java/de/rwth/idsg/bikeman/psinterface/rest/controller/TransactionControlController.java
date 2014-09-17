package de.rwth.idsg.bikeman.psinterface.rest.controller;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.bikeman.psinterface.Utils;
import de.rwth.idsg.bikeman.psinterface.dto.request.StartTransactionDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StopTransactionDTO;
import de.rwth.idsg.bikeman.psinterface.service.PedelecStationService;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by swam on 31/07/14.
 */

@RestController
@RequestMapping(value = "/psi/transaction", produces = "application/json")
@Slf4j
public class TransactionControlController {

    @Inject
    private PedelecStationService pedelecStationService;

    private static final String BASE_PATH_TRANSACTION_START = "/start";
    private static final String BASE_PATH_TRANSACTION_STOP = "/stop";

    @Timed
    @RequestMapping(value = BASE_PATH_TRANSACTION_START, method = RequestMethod.POST)
    public void startTransaction(@RequestBody StartTransactionDTO startTransactionDTO, HttpServletRequest request) throws DatabaseException {
        log.info("[From: {}] Received startTransaction: {}", Utils.getFrom(request), startTransactionDTO);
        pedelecStationService.handleStartTransaction(startTransactionDTO);
    }

    @Timed
    @RequestMapping(value = BASE_PATH_TRANSACTION_STOP, method = RequestMethod.POST)
    public void stopTransaction(@RequestBody StopTransactionDTO stopTransactionDTO, HttpServletRequest request) throws DatabaseException {
        log.info("[From: {}] Received stopTransaction: {}", Utils.getFrom(request), stopTransactionDTO);
        pedelecStationService.handleStopTransaction(stopTransactionDTO);
    }
}