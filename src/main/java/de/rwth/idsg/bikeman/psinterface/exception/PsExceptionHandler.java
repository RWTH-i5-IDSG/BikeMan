package de.rwth.idsg.bikeman.psinterface.exception;

import de.rwth.idsg.bikeman.psinterface.Utils;
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

    @ExceptionHandler(PsException.class)
    public ResponseEntity<PsExceptionMessage> processPsException(PsException e) {
        log.error("Exception happened", e);

        HttpStatus status;
        PsErrorCode errorCode = e.getErrorCode();
        switch (errorCode) {
            case NOT_REGISTERED:
                status = HttpStatus.NOT_ACCEPTABLE;
                break;

            case AUTH_ATTEMPTS_EXCEEDED:
                status = HttpStatus.FORBIDDEN;
                break;

            default:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        PsExceptionMessage msg = new PsExceptionMessage(
                Utils.nowInSeconds(),
                errorCode.name(),
                e.getMessage()
        );
        return new ResponseEntity<>(msg, status);
    }
}
