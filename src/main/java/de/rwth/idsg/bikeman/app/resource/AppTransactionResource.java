package de.rwth.idsg.bikeman.app.resource;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.bikeman.app.dto.ViewTransactionDTO;
import de.rwth.idsg.bikeman.app.service.AppCurrentCustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/app", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AppTransactionResource {

    @Autowired
    private AppCurrentCustomerService customerService;

    private static final String BASE_PATH = "/transactions";
    private static final String OPEN_PATH = "/transactions/open";

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.GET)
    public List<ViewTransactionDTO> getTransactions(HttpServletResponse response,
                                                    @RequestParam(defaultValue = "0") Integer page) {
        log.debug("REST request to get Transactions.");

        List<ViewTransactionDTO> viewTransactionDTOs = customerService.getClosedTransactions(page);

        if (viewTransactionDTOs.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }

        return viewTransactionDTOs;
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
