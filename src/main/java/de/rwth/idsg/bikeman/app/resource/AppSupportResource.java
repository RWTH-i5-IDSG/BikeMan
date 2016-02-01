package de.rwth.idsg.bikeman.app.resource;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.bikeman.app.dto.CreateFeedbackDTO;
import de.rwth.idsg.bikeman.app.dto.CreatePedelecSupportDTO;
import de.rwth.idsg.bikeman.app.dto.CreateStationSupportDTO;
import de.rwth.idsg.bikeman.app.service.AppCurrentCustomerService;
import de.rwth.idsg.bikeman.app.service.AppSupportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(value = "/app", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AppSupportResource {
    @Autowired
    private AppCurrentCustomerService customerService;

    @Autowired
    private AppSupportService appSupportService;

    private static final String FEEDBACK_PATH = "/support/feedback";
    private static final String SUPPORT_STATION_PATH = "/support/station";
    private static final String SUPPORT_PEDELEC_PATH = "/support/pedelec";

    @Timed
    @RequestMapping(value = FEEDBACK_PATH, method = RequestMethod.POST)
    public void sendFeedback (HttpServletResponse response, @Valid @RequestBody CreateFeedbackDTO data) {
        // TODO: Create a ticket in support system
        appSupportService.sendFeedbackMail(customerService.getCurrentCustomer(), data.getSubject(), data.getContent());
    }

    @Timed
    @RequestMapping(value = SUPPORT_STATION_PATH, method = RequestMethod.POST)
    public void sendStationSupport (HttpServletResponse response, @Valid @RequestBody CreateStationSupportDTO data) {
        // TODO: Create a ticket in support system
        appSupportService.sendStationSupportMail(customerService.getCurrentCustomer(), data.getStationId(), data.getError(), Optional.ofNullable(data.getContent()));
    }

    @Timed
    @RequestMapping(value = SUPPORT_PEDELEC_PATH, method = RequestMethod.POST)
    public void sendPedelecSupport (HttpServletResponse response, @Valid @RequestBody CreatePedelecSupportDTO data) {
        // TODO: Create a ticket in support system
        appSupportService.sendPedelecSupportMail(customerService.getCurrentCustomer(), data.getPedelecId(), data.getError(), Optional.ofNullable(data.getContent()));
    }



}
