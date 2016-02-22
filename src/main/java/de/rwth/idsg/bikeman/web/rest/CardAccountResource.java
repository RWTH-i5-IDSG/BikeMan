package de.rwth.idsg.bikeman.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.repository.CardAccountRepository;
import de.rwth.idsg.bikeman.service.CardAccountService;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateCardAccountBatchDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditCardAccountDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewCardAccountDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by swam on 20/10/14.
 */

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class CardAccountResource {

    @Inject
    private CardAccountRepository cardAccountRepository;

    @Inject
    private CardAccountService cardAccountService;

    private static final String ENABLE_CARDACCOUNT = "/cardaccounts/{cardId}/enable";
    private static final String DISABLE_CARDACCOUNT = "/cardaccounts/{cardId}/disable";
    private static final String BASE_CARDACCOUNTS = "/cardaccounts";
    private static final String ADD_BATCH_CARTACCOUNTS = "/cardaccounts/batch";


    @Timed
    @RequestMapping(value = ENABLE_CARDACCOUNT, method = RequestMethod.POST)
    public void enableCardAccount(@PathVariable String cardId) throws DatabaseException {
        log.debug("REST request to enable CardAccount with cardID: {}", cardId);
        cardAccountRepository.setOperationStateForCardId(OperationState.OPERATIVE, cardId);
        cardAccountRepository.resetAuthenticationTrialCount(cardId);
    }

    @Timed
    @RequestMapping(value = DISABLE_CARDACCOUNT, method = RequestMethod.POST)
    public void disableCardAccount(@PathVariable String cardId) throws DatabaseException {
        log.debug("REST request to disable CardAccount with cardID: {}", cardId);
        cardAccountRepository.setOperationStateForCardId(OperationState.INOPERATIVE, cardId);
    }

    @Timed
    @RequestMapping(value = BASE_CARDACCOUNTS, method = RequestMethod.GET)
    public List<ViewCardAccountDTO> getCardAccountsOfCurrentLogin() throws DatabaseException {
        log.debug("REST request to get all cardaccounts related to current user");
        return cardAccountService.getCardAccountDTOsOfCurrentUser();
    }

    @Timed
    @RequestMapping(value = BASE_CARDACCOUNTS, method = RequestMethod.POST)
    public void createCardAccount(@Valid @RequestBody CreateEditCardAccountDTO createEditCardAccountDTO) throws DatabaseException {
        log.debug("REST request to create new cardAccount: {}", createEditCardAccountDTO);
        cardAccountService.createCardAccount(createEditCardAccountDTO);
    }

    @Timed
    @RequestMapping(value = ADD_BATCH_CARTACCOUNTS, method = RequestMethod.POST)
    public void createCardAccounts(@Valid @RequestBody CreateCardAccountBatchDTO createCardAccountBatchDTO) throws DatabaseException {
        log.debug("REST request to create new cardAccounts: {}", createCardAccountBatchDTO);
        cardAccountService.createCardAccountWithBatch(createCardAccountBatchDTO);
    }


}
