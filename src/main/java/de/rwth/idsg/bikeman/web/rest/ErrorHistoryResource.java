package de.rwth.idsg.bikeman.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.bikeman.service.ErrorHistoryService;
import de.rwth.idsg.bikeman.web.rest.dto.view.ErrorHistoryEntryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by swam on 19/02/16.
 */

@Slf4j
@RestController
@RequestMapping("/api")
public class ErrorHistoryResource {

    @Autowired private ErrorHistoryService errorHistoryService;

    @Timed
    @RequestMapping(value = "/errorHistory", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ErrorHistoryEntryDTO> getErrorHistory() {
        return errorHistoryService.getErrorHistory();
    }
}
