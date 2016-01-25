package de.rwth.idsg.bikeman.app.resource;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.bikeman.app.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

@RequestMapping(value = "/app", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class CorsResource {

    // this method is needed for CORS "preflight"
    @Timed
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public void processOptions(HttpServletResponse response) throws AppException {
        log.debug("REST request /** method OPTIONS");
    }

}
