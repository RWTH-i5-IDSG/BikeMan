package de.rwth.idsg.bikeman.psinterface.rest;

import de.rwth.idsg.bikeman.psinterface.Utils;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 23.02.2015
 */
@ControllerAdvice(basePackages = "de.rwth.idsg.bikeman.psinterface.rest")
@Slf4j
public class PsExceptionHandler {

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<PsExceptionMessage> processDatabaseException(DatabaseException e) {
        log.error("Exception happened", e);

        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        PsExceptionMessage msg = new PsExceptionMessage(
                Utils.getSecondsOfNow(),
                e.getErrorCode(),
                e.getMessage()
        );
        return new ResponseEntity<>(msg, status);
    }
}
