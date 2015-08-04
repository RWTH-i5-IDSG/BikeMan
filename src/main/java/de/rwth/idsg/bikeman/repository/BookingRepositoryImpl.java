package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.Booking;
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
    public Booking save(Booking b) {
        em.persist(b);

        final String ixsiBookingId = IXSIConstants.Provider.id + IXSIConstants.BOOKING_ID_DELIMITER + b.getBookingId();

        b.setIxsiBookingId(ixsiBookingId);
        em.merge(b);

        return b;
    }

    @Override
    public void cancel(Booking booking) {
        // TODO: Really delete from DB or some other logic (setting a flag)?
        em.remove(booking);
    }


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
    public Booking findByIxsiBookingId(String ixsiBookingId) {
        final String query = "SELECT b FROM Booking b WHERE b.ixsiBookingId = :ixsiBookingId";

        try {
            return em.createQuery(query, Booking.class)
                .setParameter("ixsiBookingId", ixsiBookingId)
                .getSingleResult();
        } catch (NoResultException e) {
            throw new DatabaseException("Could not find booking for the given id '" + ixsiBookingId + "'", e);
        }
    }

}
