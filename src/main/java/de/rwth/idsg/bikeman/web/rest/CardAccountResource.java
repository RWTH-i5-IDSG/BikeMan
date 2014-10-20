package de.rwth.idsg.bikeman.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.repository.CardAccountRepository;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by swam on 20/10/14.
 */

@RestController
@RequestMapping("/app")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class CardAccountResource {

    @Inject
    private CardAccountRepository cardAccountRepository;

    private static final String ENABLE_CARDACCOUNT = "/rest/cardaccount/{cardId}/enable";
    private static final String DISABLE_CARDACCOUNT = "/rest/cardaccount/{cardId}/disable";

    @Timed
    @RequestMapping(value = ENABLE_CARDACCOUNT, method = RequestMethod.POST)
    public void enableCardAccount(@PathVariable String cardId) throws DatabaseException {
        log.debug("REST request to enable CardAccount with cardID: {}", cardId);
        cardAccountRepository.setOperationStateForCardId(OperationState.OPERATIVE, cardId);
    }

    @Timed
    @RequestMapping(value = DISABLE_CARDACCOUNT, method = RequestMethod.POST)
    public void disableCardAccount(@PathVariable String cardId) throws DatabaseException {
        log.debug("REST request to disable CardAccount with cardID: {}", cardId);
        cardAccountRepository.setOperationStateForCardId(OperationState.INOPERATIVE, cardId);
    }

}
