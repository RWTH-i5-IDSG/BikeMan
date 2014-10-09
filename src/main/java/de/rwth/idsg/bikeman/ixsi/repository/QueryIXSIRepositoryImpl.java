package de.rwth.idsg.bikeman.ixsi.repository;

import de.rwth.idsg.bikeman.ixsi.dto.query.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by max on 06/10/14.
 * Repository for handling
 */
@Slf4j
@Repository
public class QueryIXSIRepositoryImpl implements QueryIXSIRepository {

    @PersistenceContext private EntityManager em;

    @Override
    public BookingTargetsInfoResponseDTO bookingTargetInfos() {
        return null;
    }

    @Override
    public ChangedProvidersResponseDTO changedProviders() {
        return null;
    }

    @Override
    public AvailabilityResponseDTO availability() {
        return null;
    }

    @Override
    public CloseSessionResponseDTO closeSession() {
        return null;
    }

    @Override
    public OpenSessionResponseDTO openSession() {
        return null;
    }

    @Override
    public PlaceAvailabilityResponseDTO placeAvailability() {
        return null;
    }

    @Override
    public TokenGenerationResponseDTO tokenGeneration() {
        return null;
    }
}
