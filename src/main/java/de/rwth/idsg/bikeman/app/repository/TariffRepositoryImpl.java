package de.rwth.idsg.bikeman.app.repository;

import de.rwth.idsg.bikeman.app.dto.ViewTariffDTO;
import de.rwth.idsg.bikeman.app.exception.AppErrorCode;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.domain.*;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.List;

@Repository("TariffRepositoryImplApp")
@Slf4j
public class TariffRepositoryImpl implements TariffRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<ViewTariffDTO> findAll() throws AppException {
        final String q = "SELECT new de.rwth.idsg.bikeman.app.dto." +
                         "ViewTariffDTO(t.tariffId, t.name, t.description, t.term, t.periodicRate) " +
                         "FROM Tariff t " +
                         "WHERE t.active = true";

        try {
            List<ViewTariffDTO> tariffs = em.createQuery(q, ViewTariffDTO.class)
                    .getResultList();

            if (tariffs.isEmpty()) {
                throw new NoResultException();
            }

            return tariffs;

        } catch (NoResultException e) {
            throw new AppException("No Tariffs available.", e, AppErrorCode.CONSTRAINT_FAILED);
        } catch (Exception e) {
            throw new AppException("Failed during database operation.", e, AppErrorCode.DATABASE_OPERATION_FAILED);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public ViewTariffDTO findOne(Long tariffId) throws AppException {
        final String q = "SELECT new de.rwth.idsg.bikeman.app.dto." +
                "ViewTariffDTO(t.tariffId, t.name, t.description, t.term, t.periodicRate) " +
                "FROM Tariff t " +
                "WHERE t.tariffId = :tariffId ";

        // TODO: use jsonView to add more details to this function

        try {
            return em.createQuery(q, ViewTariffDTO.class)
                    .setParameter("tariffId", tariffId)
                    .getSingleResult();

        } catch (NoResultException e) {
            throw new AppException("Tariff " + tariffId + " not found.", e, AppErrorCode.CONSTRAINT_FAILED);
        } catch (Exception e) {
            throw new AppException("Failed during database operation.", e, AppErrorCode.DATABASE_OPERATION_FAILED);
        }
    }

}
