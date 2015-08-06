package de.rwth.idsg.bikeman.service;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.domain.*;
import de.rwth.idsg.bikeman.psinterface.dto.AccountState;
import de.rwth.idsg.bikeman.psinterface.dto.request.CardActivationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.AuthorizeConfirmationDTO;
import de.rwth.idsg.bikeman.repository.CardAccountRepository;
import de.rwth.idsg.bikeman.repository.TariffRepository;
import de.rwth.idsg.bikeman.repository.UserRepository;
import de.rwth.idsg.bikeman.security.SecurityUtils;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditCardAccountDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewCardAccountDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.LocalDateTime;
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

    @Inject private CardAccountRepository cardAccountRepository;
    @Inject private UserRepository userRepository;
    @Inject private TariffRepository tariffRepository;

    public Optional<AuthorizeConfirmationDTO> activateCardAccount(CardActivationDTO cardActivationDTO) {
        CardAccount cardAccount = cardAccountRepository.findByActivationKey(cardActivationDTO.getActivationKey());
        if (cardAccount == null) {
            return Optional.absent();
        }

        cardAccount.setOperationState(OperationState.OPERATIVE);
        cardAccount.setActivationKey(null);
        cardAccount.setCardPin(cardActivationDTO.getCardPin());
        cardAccountRepository.save(cardAccount);

        AuthorizeConfirmationDTO dto = new AuthorizeConfirmationDTO(cardAccount.getCardId(), AccountState.HAS_NO_PEDELEC);
        return Optional.of(dto);
    }

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

        User user;


        // when no login exists, add to current user
        if (createEditCardAccountDTO.getLogin() == null) {
            user = userRepository.findByLoginIgnoreCase(SecurityUtils.getCurrentLogin());
        } else {
            user = userRepository.findByLoginIgnoreCase(createEditCardAccountDTO.getLogin());
        }

        BookedTariff bookedTariff = new BookedTariff();
        bookedTariff.setTariff(tariffRepository.findByName(createEditCardAccountDTO.getTariff()));
        bookedTariff.setBookedFrom(LocalDateTime.now());
        // set the bookedUntil date to null, if no subscription term is declared
        if (tariffRepository.findByName(createEditCardAccountDTO.getTariff()).getTerm() == null) {
            bookedTariff.setBookedUntil(null);
        } else {
            bookedTariff.setBookedUntil(new LocalDateTime().plusDays(
                    tariffRepository.findByName(createEditCardAccountDTO.getTariff()).getTerm()
            ));
        }

        CardAccount cardAccount = new CardAccount();
        cardAccount.setCardId(createEditCardAccountDTO.getCardId());
        cardAccount.setCardPin(createEditCardAccountDTO.getCardPin());
        cardAccount.setActivationKey(RandomStringUtils.randomNumeric(12));
        cardAccount.setOperationState(OperationState.OPERATIVE);
        cardAccount.setOwnerType(CustomerType.MAJOR_CUSTOMER);
        cardAccount.setUser(user);
        cardAccount.setCurrentTariff(bookedTariff);

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
