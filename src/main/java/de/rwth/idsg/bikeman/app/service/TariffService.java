package de.rwth.idsg.bikeman.app.service;

import de.rwth.idsg.bikeman.app.dto.ViewTariffDTO;
import de.rwth.idsg.bikeman.app.repository.TariffRepository;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;


@Service("TariffServiceApp")
@Slf4j
public class TariffService {

    @Autowired
    private TariffRepository tariffRepository;

    @Inject
    private de.rwth.idsg.bikeman.service.TariffService tariffService;

    public List<ViewTariffDTO> getAll() {
        return tariffRepository.findAll();
    }

    public ViewTariffDTO get(Long id) throws DatabaseException {
        ViewTariffDTO dto = tariffRepository.findOne(id);
        dto.setPriceList( tariffService.listPrice(dto.getName()) );

        return dto;
    }

}