package de.rwth.idsg.bikeman.service;

import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.repository.CardAccountRepository;
import de.rwth.idsg.bikeman.security.SecurityUtils;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewCardAccountDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swam on 20/10/14.
 */

@Service
@Slf4j
public class CardAccountService {

    @Inject
    private CardAccountRepository cardAccountRepository;

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

    private ViewCardAccountDTO convertCardAccount(CardAccount cardAccount) {
        return ViewCardAccountDTO.builder()
                .cardId(cardAccount.getCardId())
                .cardPin(cardAccount.getCardPin())
                .inTransaction(cardAccount.getInTransaction())
                .operationState(cardAccount.getOperationState())
                .build();
    }
}
