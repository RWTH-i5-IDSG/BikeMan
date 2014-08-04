package de.rwth.idsg.velocity.psinterface.exception;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by swam on 04/08/14.
 */

@ControllerAdvice
@Slf4j
public class GeneralPSInterfaceExceptionHandler {

    @ExceptionHandler(PSInterfaceException.class)
    public ResponseEntity<ErrorMessage> psInterfaceException(PSInterfaceException e) {
        log.error("Exception happened", e);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorMessage msg = new ErrorMessage(
                DateTime.now().getMillis(),
                e.getErrorCode(),
                e.getMessage()
        );

        return new ResponseEntity<>(msg, status);
    }
}
