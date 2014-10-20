package de.rwth.idsg.bikeman.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.repository.CardAccountRepository;
import de.rwth.idsg.bikeman.service.CardAccountService;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewCardAccountDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

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

    @Inject
    private CardAccountService cardAccountService;

    private static final String ENABLE_CARDACCOUNT = "/rest/cardaccounts/{cardId}/enable";
    private static final String DISABLE_CARDACCOUNT = "/rest/cardaccounts/{cardId}/disable";
    private static final String CURRENTUSER_CARDACCOUNTS = "/rest/cardaccounts";


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

    @Timed
    @RequestMapping(value = CURRENTUSER_CARDACCOUNTS, method = RequestMethod.GET)
    public List<ViewCardAccountDTO> getCardAccountsOfCurrentLogin() throws DatabaseException {
        log.debug("REST request to get all cardaccounts related to current user");
        return cardAccountService.getCardAccountsOfCurrentUser();
    }



}
