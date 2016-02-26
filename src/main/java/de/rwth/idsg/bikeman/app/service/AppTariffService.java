package de.rwth.idsg.bikeman.app.service;

import de.rwth.idsg.bikeman.app.dto.ViewTariffDTO;
import de.rwth.idsg.bikeman.app.repository.AppTariffRepository;
import de.rwth.idsg.bikeman.service.TariffService;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;


@Service
@Slf4j
public class AppTariffService {

    @Autowired
    private AppTariffRepository appTariffRepository;

    @Inject
    private TariffService tariffService;

    public List<ViewTariffDTO> getAll() {
        return appTariffRepository.findAll();
    }

    public ViewTariffDTO get(Long id) throws DatabaseException {
        ViewTariffDTO dto = appTariffRepository.findOne(id);
        dto.setPriceList(tariffService.listPrice(dto.getName()));

        return dto;
    }

}
