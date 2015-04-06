package de.rwth.idsg.bikeman.app.repository;

import de.rwth.idsg.bikeman.app.dto.ChangeTariffDTO;
import de.rwth.idsg.bikeman.app.dto.ViewTariffDTO;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.domain.Tariff;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TariffRepository {

    public List<ViewTariffDTO> findAll() throws AppException;
    public ViewTariffDTO findOne(Long stationId) throws AppException;

}
