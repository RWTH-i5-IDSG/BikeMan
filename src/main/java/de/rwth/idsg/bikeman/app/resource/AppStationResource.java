package de.rwth.idsg.bikeman.app.resource;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.bikeman.app.dto.RentPedelecDTO;
import de.rwth.idsg.bikeman.app.dto.ViewPedelecSlotDTO;
import de.rwth.idsg.bikeman.app.dto.ViewStationDTO;
import de.rwth.idsg.bikeman.app.dto.ViewStationSlotsDTO;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.app.service.AppBookingService;
import de.rwth.idsg.bikeman.app.service.AppCurrentCustomerService;
import de.rwth.idsg.bikeman.app.service.AppPedelecService;
import de.rwth.idsg.bikeman.app.service.AppStationService;
import de.rwth.idsg.bikeman.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value = "/app", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AppStationResource {

    @Autowired
    private AppStationService appStationService;

    @Autowired
    private AppPedelecService appPedelecService;

    @Autowired
    private AppBookingService appBookingService;

    @Autowired
    private AppCurrentCustomerService appCurrentCustomerService;

    private static final String BASE_PATH = "/stations";
    private static final String ID_PATH = "/stations/{id}";
    private static final String SLOT_PATH = "/stations/{id}/slots";
    private static final String RECOMMEND_PATH = "/stations/{id}/recommended-pedelec";
    private static final String RENTAL_PATH = "/stations/{stationId}/rent";

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.GET)
    public List<ViewStationDTO> getAll() throws AppException {
        log.debug("REST request to get all Stations");
        return appStationService.getAll();
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.GET)
    public ViewStationDTO get(@PathVariable Long id) throws AppException {
        log.info("REST request to get Station : {}", id);
        return appStationService.get(id);
    }

    @Timed
    @RequestMapping(value = SLOT_PATH, method = RequestMethod.GET)
    public ViewStationSlotsDTO getSlots(@PathVariable Long id) throws AppException {
        if (SecurityUtils.isAuthenticated()) {
            log.info("REST request to get Slots (for authenticated customer) of Station : {}", id);
            return appStationService.getSlotsWithPreferredSlotId(id, appCurrentCustomerService.getCurrentCustomer());
        } else {
            log.info("REST request to get Slots of Station : {}", id);
            return appStationService.getSlots(id);
        }
    }

    @Timed
    @RequestMapping(value = RECOMMEND_PATH, method = RequestMethod.GET)
    public ViewPedelecSlotDTO getRecommendedPedelec(@PathVariable Long id, HttpServletResponse response) throws AppException {
        log.debug("REST request to get a suggestion for renting a pedelec");

        Optional<ViewPedelecSlotDTO> optional = appBookingService.getSlot(appCurrentCustomerService.getCurrentCustomer());
        if (optional.isPresent()) {
            return optional.get();
        }

        Optional<ViewPedelecSlotDTO> optionalPedelec = appPedelecService.getRecommendedPedelec(id);

        if (optionalPedelec.isPresent()) {
            return optionalPedelec.get();
        }

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        return null;
    }

    @Timed
    @RequestMapping(value = RENTAL_PATH, method = RequestMethod.POST)
    public ViewPedelecSlotDTO rentPedelec(HttpServletResponse response,
                                          @Valid @RequestBody RentPedelecDTO dto,
                                          @PathVariable Long stationId
                                          ) {
        log.debug("REST request to remotely rent a pedelec.");

        return appStationService.authorizeRemote(
                stationId,
                dto.getStationSlotId(),
                appCurrentCustomerService.getCurrentCustomer(),
                dto.getCardPin()
            );
    }

}
