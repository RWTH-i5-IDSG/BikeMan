package de.rwth.idsg.velocity.psinterface.rest.controller;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.psinterface.dto.request.StartTransactionDTO;
import de.rwth.idsg.velocity.psinterface.dto.request.StopTransactionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by swam on 31/07/14.
 */

@RestController
@RequestMapping("/psi/transaction")
@Slf4j
public class TransactionControlResource {

    @RequestMapping(value = "/start",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public void startTransaction(StartTransactionDTO startTransactionDTO) {

    }

    @RequestMapping(value = "/stop",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public void stopTransaction(StopTransactionDTO stopTransactionDTO) {

    }
}
