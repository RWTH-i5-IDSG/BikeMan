package de.rwth.idsg.bikeman.app.resource;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;
import de.rwth.idsg.bikeman.app.dto.ViewTariffDTO;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.app.service.AppTariffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/app", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AppTariffResource {

    @Autowired
    private AppTariffService appTariffService;

    private static final String BASE_PATH = "/tariffs";
    private static final String ID_PATH = "/tariffs/{id}";

    @Timed
    @JsonView(ViewTariffDTO.ListView.class)
    @RequestMapping(value = BASE_PATH, method = RequestMethod.GET)
    public List<ViewTariffDTO> getAll() throws AppException {
        log.debug("REST request to get all Tariffs");
        return appTariffService.getAll();
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.GET)
    public ViewTariffDTO get(@PathVariable Long id) throws AppException {
        log.info("REST request to get Tariff : {}", id);
        return appTariffService.get(id);
    }

}
