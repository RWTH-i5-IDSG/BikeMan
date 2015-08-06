package de.rwth.idsg.bikeman.psinterface.repository;

import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.domain.Reservation;
import de.rwth.idsg.bikeman.domain.Transaction;
import de.rwth.idsg.bikeman.repository.BookingRepository;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 16.06.2015
 */
@Repository
@Slf4j
public class PsiBookingRepositoryImpl implements PsiBookingRepository {

    @PersistenceContext private EntityManager em;
    @Inject private BookingRepository bookingRepository;

    @Override
    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public Booking findByTransaction(Transaction transaction) {
        final String query = "SELECT b FROM Booking b WHERE b.transaction = :transaction";
        try {
            return em.createQuery(query, Booking.class)
                     .setParameter("transaction", transaction)
                     .getSingleResult();
        } catch (NoResultException e) {
            throw new DatabaseException("Could not find booking for specified transaction.", e);
        }
    }

    @Override
    public Booking findByReservation(Reservation reservation) {
        final String query = "SELECT b FROM Booking b WHERE b.reservation = :reservation";
        try {
            return em.createQuery(query, Booking.class)
                     .setParameter("reservation", reservation)
                     .getSingleResult();
        } catch (NoResultException e) {
            throw new DatabaseException("Could not find booking for specified reservation.", e);
        }
    }

}
