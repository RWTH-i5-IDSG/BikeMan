package de.rwth.idsg.bikeman.app.service;

import de.rwth.idsg.bikeman.app.dto.ViewTariffDTO;
import de.rwth.idsg.bikeman.app.repository.TariffRepository;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("TariffServiceApp")
@Slf4j
public class TariffService {

    @Autowired
    private TariffRepository tariffRepository;

    public List<ViewTariffDTO> getAll() {
        return tariffRepository.findAll();
    }

    public ViewTariffDTO get(Long id) throws DatabaseException {
        return tariffRepository.findOne(id);
    }

}