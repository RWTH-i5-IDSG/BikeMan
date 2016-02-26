package de.rwth.idsg.bikeman.app.repository;

import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.domain.Pedelec;

import java.util.List;

public interface AppPedelecRepository {

    List<Pedelec> findAvailablePedelecs(Long stationId) throws AppException;

}
