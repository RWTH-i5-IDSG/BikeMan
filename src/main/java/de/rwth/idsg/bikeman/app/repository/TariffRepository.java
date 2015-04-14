package de.rwth.idsg.bikeman.app.repository;

import de.rwth.idsg.bikeman.app.dto.ViewTariffDTO;
import de.rwth.idsg.bikeman.app.exception.AppException;

import java.util.List;

public interface TariffRepository {

    List<ViewTariffDTO> findAll() throws AppException;
    ViewTariffDTO findOne(Long stationId) throws AppException;

}
