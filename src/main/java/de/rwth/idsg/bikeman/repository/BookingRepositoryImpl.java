package de.rwth.idsg.bikeman.repository;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.domain.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 08.01.2015
 */
@Repository
@Slf4j
public class BookingRepositoryImpl implements BookingRepository {

    @PersistenceContext private EntityManager em;

    @Override
    @Transactional(readOnly = false)
    public long saveAndGetId(Booking b) {
        em.persist(b);
        em.flush();
        return b.getBookingId();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Long> findIdByTransaction(Transaction transaction) {
        final String query = "SELECT b.bookingId FROM Booking b WHERE b.transaction = :transaction";
        try {
            Long boid = em.createQuery(query, Long.class)
                          .setParameter("transaction", transaction)
                          .getSingleResult();
            return Optional.of(boid);

        } catch (NoResultException e) {
            return Optional.absent();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> findClosedBookings(List<Long> bookingIdList) {
        final String query = "SELECT b FROM Booking b WHERE b.bookingId IN (:bookingIdList) " +
                             "AND b.transaction.endDateTime IS NOT NULL " +
                             "AND b.transaction.toSlot IS NOT NULL";

        return em.createQuery(query, Booking.class)
                 .setParameter("bookingIdList", bookingIdList)
                 .getResultList();
    }

}
