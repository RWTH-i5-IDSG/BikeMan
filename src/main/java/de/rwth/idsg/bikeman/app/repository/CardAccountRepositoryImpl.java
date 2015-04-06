package de.rwth.idsg.bikeman.app.repository;

import de.rwth.idsg.bikeman.app.exception.AppErrorCode;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.domain.BookedTariff;
import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.repository.TariffRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Slf4j
public class CardAccountRepositoryImpl implements CardAccountRepository {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private TariffRepository tariffRepository;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setCurrentTariff(Long cardAccountId, Long tariffId) {
        CardAccount cardAccount = em.find(CardAccount.class, cardAccountId);

        BookedTariff updateBookedTariff = new BookedTariff();
        updateBookedTariff.setBookedFrom(new LocalDateTime());

        if (tariffRepository.findByTariffId(tariffId).getTerm() == null) {
            updateBookedTariff.setBookedUntil(null);
        } else {
            updateBookedTariff.setBookedUntil(new LocalDateTime().plusDays(
                    tariffRepository.findByTariffId(tariffId).getTerm()
            ));
        }

        updateBookedTariff.setTariff(tariffRepository.findByTariffId(tariffId));
        cardAccount.setCurrentTariff(updateBookedTariff);

        try {
            em.merge(cardAccount);
            log.debug("Updated cardAccount {}", cardAccountId);

        } catch (Exception e) {
            throw new AppException("Failed to update cardAccount", e, AppErrorCode.DATABASE_OPERATION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setAutomaticRenewal(Long cardAccountId, Boolean status) {
        CardAccount cardAccount = em.find(CardAccount.class, cardAccountId);
        cardAccount.setAutoRenewTariff(status);

        try {
            em.merge(cardAccount);
            log.debug("Updated cardAccount {}", cardAccountId);

        } catch (Exception e) {
            throw new AppException("Failed to update cardAccount", e, AppErrorCode.DATABASE_OPERATION_FAILED);
        }
    }
}
