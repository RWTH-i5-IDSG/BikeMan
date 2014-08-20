package de.rwth.idsg.velocity.psinterface.rest.controller;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.psinterface.dto.request.StartTransactionDTO;
import de.rwth.idsg.velocity.psinterface.dto.request.StopTransactionDTO;
import de.rwth.idsg.velocity.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by swam on 31/07/14.
 */

@RestController
@RequestMapping(value = "/psi/transaction", produces = "application/json")
@Slf4j
public class TransactionControlController {

    private static final String BASE_PATH_TRANSACTION_START = "/start";
    private static final String BASE_PATH_TRANSACTION_STOP = "/stop";


    @RequestMapping(value = BASE_PATH_TRANSACTION_START,
            method = RequestMethod.POST)
    @Timed
    public void startTransaction(@RequestBody StartTransactionDTO startTransactionDTO) {

    }

    @RequestMapping(value = BASE_PATH_TRANSACTION_STOP,
            method = RequestMethod.POST)
    @Timed
    public void stopTransaction(@RequestBody StopTransactionDTO stopTransactionDTO) {

    }
}
