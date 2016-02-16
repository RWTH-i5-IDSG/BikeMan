package de.rwth.idsg.bikeman.service;

import de.rwth.idsg.bikeman.domain.BookedTariff;
import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.domain.CustomerType;
import de.rwth.idsg.bikeman.domain.Manager;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.domain.TariffType;
import de.rwth.idsg.bikeman.repository.CardAccountRepository;
import de.rwth.idsg.bikeman.repository.ManagerRepository;
import de.rwth.idsg.bikeman.repository.TariffRepository;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditManagerDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Wolfgang Kluth on 16/02/16.
 */

@Service
@Transactional
public class ManagerService {

    @Autowired private ManagerRepository managerRepository;
    @Autowired private TariffRepository tariffRepository;
    @Autowired private CardAccountRepository cardAccountRepository;

    public void createManager(CreateEditManagerDTO managerDTO) {
        Manager manager = managerRepository.create(managerDTO);

        BookedTariff bookedTariff = new BookedTariff();
        bookedTariff.setTariff(tariffRepository.findByName(TariffType.TestSystemTariff));
        bookedTariff.setBookedFrom(LocalDateTime.now());
        // set the bookedUntil date to null, if no subscription term is declared
        if (tariffRepository.findByName(TariffType.TestSystemTariff).getTerm() == null) {
            bookedTariff.setBookedUntil(null);
        } else {
            bookedTariff.setBookedUntil(new LocalDateTime().plusDays(
                    tariffRepository.findByName(TariffType.TestSystemTariff).getTerm()
            ));
        }

        CardAccount cardAccount = new CardAccount();

        if (managerDTO.getCardId() != null) {
            cardAccount.setCardId(managerDTO.getCardId());
            cardAccount.setCardPin(managerDTO.getCardPin());
        }

        cardAccount.setOperationState(OperationState.OPERATIVE);
        cardAccount.setOwnerType(CustomerType.FLEET_MANAGER);
        cardAccount.setUser(manager);
        cardAccount.setCurrentTariff(bookedTariff);

        try {
            cardAccountRepository.save(cardAccount);
        } catch (Throwable e) {
            throw new DatabaseException("CardId already exists.");
        }
    }

    public void updateManager(CreateEditManagerDTO managerDTO) {
        Manager manager = managerRepository.update(managerDTO);

        CardAccount cardAccount = manager.getCardAccount();
        cardAccount.setOwnerType(CustomerType.CUSTOMER);
        if (managerDTO.getCardId() != null) {
            cardAccount.setCardId(managerDTO.getCardId());
            cardAccount.setCardPin(managerDTO.getCardPin());
        }
    }

}
