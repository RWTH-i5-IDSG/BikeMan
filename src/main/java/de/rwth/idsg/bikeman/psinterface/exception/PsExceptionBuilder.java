package de.rwth.idsg.bikeman.psinterface.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * For incoming messages (responses from station)
 *
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 22.06.2015
 */
@Slf4j
@Component
public class PsExceptionBuilder {

    private final ObjectReader objectReader = new ObjectMapper().reader(PsExceptionMessage.class);

    public PsException build(String errorString) {
        log.error("Error message received: {}", errorString);
        try {
            PsExceptionMessage errorMessage = objectReader.readValue(errorString);
            return new PsException(errorMessage.getMessage(), errorMessage.getCode());

        } catch (IOException e) {
            return new PsException("Station responded with an error, which could not be parsed",
                e, PsErrorCode.UNKNOWN_SERVER_ERROR);
        }
    }

    public PsException buildFromMsg(String errorString) {
        return new PsException(errorString, PsErrorCode.UNKNOWN_SERVER_ERROR);
    }
}
