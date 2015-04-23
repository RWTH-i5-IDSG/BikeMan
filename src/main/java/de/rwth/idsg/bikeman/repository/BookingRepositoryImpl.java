package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.domain.Transaction;
import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
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

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = false)
    public Booking save(Booking b) {
        em.persist(b);

        final String ixsiBookingId = IXSIConstants.Provider.id + IXSIConstants.BOOKING_ID_DELIMITER + b.getBookingId();

        b.setIxsiBookingId(ixsiBookingId);
        em.merge(b);

        return b;
    }

//    @Override
//    @Transactional
//    public void createIxsiBookingId(Long bookingId) {
//
//        final String ixsiBookingId = IXSIConstants.Provider.id + IXSIConstants.BOOKING_ID_DELIMITER + bookingId;
//
//        final String query = "UPDATE Booking b SET b.ixsiBookingId = :ixsiBookingId WHERE b.bookingId = :bookingId";
//
//        em.createQuery(query)
//            .setParameter("ixsiBookingId", ixsiBookingId)
//            .setParameter("bookingId", bookingId)
//            .executeUpdate();
//
//    }

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

//    @Override
//    @Transactional(readOnly = true)
//    public Long findIdByTransaction(Transaction transaction) {
//        final String query = "SELECT b.bookingId FROM Booking b WHERE b.transaction = :transaction";
//        try {
//            return em.createQuery(query, Long.class)
//                .setParameter("transaction", transaction)
//                .getSingleResult();
//
//        } catch (NoResultException e) {
//            throw new DatabaseException("Could not find booking for specified transaction.", e);
//        }
//    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> findClosedBookings(List<String> ixsiBookingIdList) {
        final String query = "SELECT b FROM Booking b WHERE b.ixsiBookingId IN (:ixsiBookingIdList) " +
            "AND b.transaction.endDateTime IS NOT NULL " +
            "AND b.transaction.toSlot IS NOT NULL";

        return em.createQuery(query, Booking.class)
            .setParameter("ixsiBookingIdList", ixsiBookingIdList)
            .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Booking findOne(Long id) {
        final String query = "SELECT b FROM Booking b WHERE b.bookingId = :bookingId";

        return em.createQuery(query, Booking.class)
            .setParameter("bookingId", id)
            .getSingleResult();
    }

}
