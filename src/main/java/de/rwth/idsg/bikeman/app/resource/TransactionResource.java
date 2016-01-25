package de.rwth.idsg.bikeman.app.resource;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.bikeman.app.dto.ViewTransactionDTO;
import de.rwth.idsg.bikeman.app.service.CurrentCustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController("TransactionResourceApp")
@RequestMapping(value = "/app", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class TransactionResource {

    @Autowired
    private CurrentCustomerService customerService;

    private static final String BASE_PATH = "/transactions";
    private static final String OPEN_PATH = "/transactions/open";

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.GET)
    public List<ViewTransactionDTO> getTransactions() {
        log.debug("REST request to get Transactions.");

        return customerService.getClosedTransactions();
    }

    @Timed
    @RequestMapping(value = OPEN_PATH, method = RequestMethod.GET)
    public ViewTransactionDTO getOpenTransaction(HttpServletResponse response) {
        log.debug("REST request to get open Transaction.");

        Optional<ViewTransactionDTO> optional = customerService.getOpenTransaction();
        if (optional.isPresent()) {
            return optional.get();
        }

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        return null;
    }
}
