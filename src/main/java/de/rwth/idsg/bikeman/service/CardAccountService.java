package de.rwth.idsg.bikeman.service;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.domain.BookedTariff;
import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.domain.CustomerType;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.domain.TariffType;
import de.rwth.idsg.bikeman.domain.User;
import de.rwth.idsg.bikeman.psinterface.dto.request.CardActivationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.CardActivationResponseDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.CardWriteKeyDTO;
import de.rwth.idsg.bikeman.psinterface.repository.PsiStationRepository;
import de.rwth.idsg.bikeman.repository.CardAccountRepository;
import de.rwth.idsg.bikeman.repository.TariffRepository;
import de.rwth.idsg.bikeman.repository.UserRepository;
import de.rwth.idsg.bikeman.security.SecurityUtils;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateCardAccountBatchDTO;
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
    @Inject private PsiStationRepository stationRepository;

    @Transactional(readOnly = true)
    public Optional<CardActivationResponseDTO> activateCardAccount(CardActivationDTO cardActivationDTO) {
        CardAccount cardAccount = cardAccountRepository.findByActivationKey(cardActivationDTO.getActivationKey());
        if (cardAccount == null) {
            return Optional.absent();
        }

        cardAccount.setOperationState(OperationState.INOPERATIVE);
        cardAccount.setActivationKey(null);
        cardAccount.setCardPin(cardActivationDTO.getCardPin());
        cardAccountRepository.save(cardAccount);

        CardWriteKeyDTO keys = stationRepository.getCardWriteKey();

        CardActivationResponseDTO dto = CardActivationResponseDTO.builder()
                                                                 .cardId(cardAccount.getCardId())
                                                                 .readKey(keys.getReadKey())
                                                                 .writeKey(keys.getWriteKey())
                                                                 .applicationKey(keys.getApplicationKey())
                                                                 .build();

        return Optional.of(dto);
    }

    public void setCardOperative(String cardId) {
        cardAccountRepository.setOperationStateForCardId(OperationState.OPERATIVE, cardId);
    }

    @Transactional(readOnly = true)
    public List<ViewCardAccountDTO> getCardAccountDTOsOfCurrentUser() {

        String userLogin = SecurityUtils.getCurrentLogin();
        List<CardAccount> cardAccountList = cardAccountRepository.findByUserLogin(userLogin);

        List<ViewCardAccountDTO> viewCardAccountDTOList = new ArrayList<>();

        for (CardAccount cardAccount : cardAccountList) {
            viewCardAccountDTOList.add(this.convertCardAccount(cardAccount));
        }

        return viewCardAccountDTOList;
    }

    @Transactional(readOnly = true)
    public List<CardAccount> getCardAccountsOfCurrentUser() {

        String userLogin = SecurityUtils.getCurrentLogin();

        return cardAccountRepository.findByUserLogin(userLogin);
    }

    @Transactional
    public void createCardAccount(CreateEditCardAccountDTO createEditCardAccountDTO) throws DatabaseException {

        createCardAccount(createEditCardAccountDTO.getLogin(), createEditCardAccountDTO.getTariff(), createEditCardAccountDTO.getCardId(), createEditCardAccountDTO.getCardPin());

    }

    @Transactional
    public void createCardAccountWithBatch(CreateCardAccountBatchDTO createCardAccountBatchDTO) {

        // convert string into hashmap
        String lines[] = createCardAccountBatchDTO.getBatch().split("\\r?\\n");

        for (String line : lines) {
            String idAndKey[] = line.split(",");

            createCardAccount(createCardAccountBatchDTO.getLogin(), createCardAccountBatchDTO.getTariff(), idAndKey[0], idAndKey[1]);
        }
    }

    private void createCardAccount(String login, TariffType tariffType, String cardId, String cardPin) {

        User user = checkAndGetUser(login);
        BookedTariff bookedTariff = getBookedTariff(tariffType);

        CardAccount cardAccount = new CardAccount();
        cardAccount.setCardId(cardId);
        cardAccount.setCardPin(cardPin);
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

    private BookedTariff getBookedTariff(TariffType tariffType) {
        BookedTariff bookedTariff = new BookedTariff();
        bookedTariff.setTariff(tariffRepository.findByName(tariffType));
        bookedTariff.setBookedFrom(LocalDateTime.now());
        // set the bookedUntil date to null, if no subscription term is declared
        if (tariffRepository.findByName(tariffType).getTerm() == null) {
            bookedTariff.setBookedUntil(null);
        } else {
            bookedTariff.setBookedUntil(new LocalDateTime().plusDays(
                    tariffRepository.findByName(tariffType).getTerm()
            ));
        }
        return bookedTariff;
    }

    private User checkAndGetUser(String login) {
        User user;// when no login exists, add to current user
        if (login == null) {
            user = userRepository.findByLoginIgnoreCase(SecurityUtils.getCurrentLogin());
        } else {
            user = userRepository.findByLoginIgnoreCase(login);
        }
        return user;
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
