package de.rwth.idsg.bikeman.app.resource;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.bikeman.app.dto.ViewBookingDTO;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.app.service.BookingService;
import de.rwth.idsg.bikeman.app.service.CurrentCustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


@RestController("BookingResourceApp")
@RequestMapping(value = "/app", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class BookingResource {

    @Autowired
    private CurrentCustomerService customerService;

    @Autowired
    private BookingService bookingService;

    private static final String BOOKING_PATH = "/stations/{stationId}/book";
    private static final String VIEW_DELETE_BOOKING_PATH = "/booking";

    @Timed
    @Transactional(readOnly = false)
    @RequestMapping(value = BOOKING_PATH, method = RequestMethod.POST)
    public ViewBookingDTO create(@PathVariable Long stationId, HttpServletResponse response) throws AppException {
        log.debug("REST request to create a booking at station " + stationId);
        Optional<ViewBookingDTO> optional = bookingService.create(stationId, customerService.getCurrentCustomer());

        if (optional.isPresent()) {
            return optional.get();
        }

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        return null;
    }

    @Timed
    @RequestMapping(value = VIEW_DELETE_BOOKING_PATH, method = RequestMethod.GET)
    public ViewBookingDTO get(HttpServletResponse response) throws AppException {
        log.info("REST request to get current Booking.");
        Optional<ViewBookingDTO> optional = bookingService.getDTO(customerService.getCurrentCustomer());

        if (optional.isPresent()) {
            return optional.get();
        }

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        return null;
    }

    @Timed
    @RequestMapping(value = VIEW_DELETE_BOOKING_PATH, method = RequestMethod.DELETE)
    public void delete() throws AppException {
        log.info("REST request to delete current Booking");
        bookingService.delete(customerService.getCurrentCustomer());
    }

}
