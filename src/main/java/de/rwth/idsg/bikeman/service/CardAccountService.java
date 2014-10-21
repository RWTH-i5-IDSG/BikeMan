package de.rwth.idsg.bikeman.service;

import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.domain.CustomerType;
import de.rwth.idsg.bikeman.domain.MajorCustomer;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.repository.CardAccountRepository;
import de.rwth.idsg.bikeman.repository.UserRepository;
import de.rwth.idsg.bikeman.security.SecurityUtils;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditCardAccountDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewCardAccountDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by swam on 20/10/14.
 */

@Service
@Slf4j
public class CardAccountService {

    @Inject
    private CardAccountRepository cardAccountRepository;

    @Inject
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ViewCardAccountDTO> getCardAccountsOfCurrentUser() {

        String userLogin = SecurityUtils.getCurrentLogin();
        List<CardAccount> cardAccountList = cardAccountRepository.findByUserLogin(userLogin);

        List<ViewCardAccountDTO> viewCardAccountDTOList = new ArrayList<>();

        for (CardAccount cardAccount : cardAccountList) {
            viewCardAccountDTOList.add(this.convertCardAccount(cardAccount));
        }

        return viewCardAccountDTOList;
    }

    @Transactional
    public void createCardAccount(CreateEditCardAccountDTO createEditCardAccountDTO) throws DatabaseException {

        MajorCustomer currentMajorCustomer = (MajorCustomer)userRepository.findByLoginIgnoreCase(SecurityUtils.getCurrentLogin());

        CardAccount cardAccount = CardAccount.builder()
                .cardId(createEditCardAccountDTO.getCardId())
                .cardPin(createEditCardAccountDTO.getCardPin())
                .inTransaction(false)
                .operationState(OperationState.OPERATIVE)
                .ownerType(CustomerType.MAJOR_CUSTOMER)
                .user(currentMajorCustomer)
                .build();

        try {
            cardAccountRepository.save(cardAccount);
        } catch (Throwable e) {
            throw new DatabaseException("CardId already exists.");
        }

    }

    private ViewCardAccountDTO convertCardAccount(CardAccount cardAccount) {
        return ViewCardAccountDTO.builder()
                .cardId(cardAccount.getCardId())
                .cardPin(cardAccount.getCardPin())
                .inTransaction(cardAccount.getInTransaction())
                .operationState(cardAccount.getOperationState())
                .build();
    }
}
