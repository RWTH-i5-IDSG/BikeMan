package de.rwth.idsg.bikeman.app.exception;

import de.rwth.idsg.bikeman.psinterface.Utils;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 23.02.2015
 */
@ControllerAdvice(basePackages = "de.rwth.idsg.bikeman.app.resource")
@Slf4j
public class AppExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(AppException.class)
    public ResponseEntity<AppExceptionMessage> processAppException(AppException e) {
        log.debug("Exception happened", e);

        HttpStatus status;
        AppErrorCode errorCode = e.getErrorCode();
        switch (errorCode) {
            case CONSTRAINT_FAILED:
                status = HttpStatus.BAD_REQUEST;
                break;

            case NOT_REGISTERED:
            case RENTAL_BLOCKED:
            case BOOKING_BLOCKED:
                status = HttpStatus.NOT_ACCEPTABLE;
                break;

            case AUTH_ATTEMPTS_EXCEEDED:
            case AUTH_FAILED:
                status = HttpStatus.FORBIDDEN;
                break;

            default:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        AppExceptionMessage msg = new AppExceptionMessage(
                Utils.nowInSeconds(),
                errorCode.name(),
                e.getMessage()
        );
        return new ResponseEntity<>(msg, status);
    }

    @ExceptionHandler({HibernateException.class, PersistenceException.class, DatabaseException.class})
    public ResponseEntity<AppExceptionMessage> processDatabaseException(Exception e) {
        log.error("Exception happened", e);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        AppExceptionMessage msg = new AppExceptionMessage(
                Utils.nowInSeconds(),
                AppErrorCode.DATABASE_OPERATION_FAILED.name(),
                e.getMessage()
        );
        return new ResponseEntity<>(msg, status);
    }


    /**
     * Catches the controller path errors of the Rest API.
     *
     * Example: If a controller declares an integer in the path, but frontend sends anything but an integer.
     */
    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<AppExceptionMessage> processTypeException(TypeMismatchException e) {
        log.error("Exception happened", e);

        HttpStatus status = HttpStatus.BAD_REQUEST;
        AppExceptionMessage msg = new AppExceptionMessage(
                status.value(),
                status.getReasonPhrase(),
                e.getMessage()
        );
        return new ResponseEntity<>(msg, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AppExceptionMessage> processValidationException(MethodArgumentNotValidException e) {
        log.error("Exception happened", e);

        Locale currentLocale =  LocaleContextHolder.getLocale();

        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : errors) {
            String localizedError = fieldError.getField() + ": " + messageSource.getMessage(fieldError, currentLocale);
            errorMessages.add(localizedError);
        }

        HttpStatus status = HttpStatus.BAD_REQUEST;
        AppExceptionMessage msg = new AppExceptionMessage(
            status.value(),
            status.getReasonPhrase(),
            "Validation failed for the submitted form"
        );
        msg.setFieldErrors(errorMessages);
        return new ResponseEntity<>(msg, status);
    }

    // -------------------------------------------------------------------------
    // Fall-back
    // -------------------------------------------------------------------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppExceptionMessage> processException(Exception e) {
        log.error("Exception happened", e);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        AppExceptionMessage msg = new AppExceptionMessage(
                Utils.nowInSeconds(),
                AppErrorCode.UNKNOWN_SERVER_ERROR.name(),
                e.getMessage()
        );
        return new ResponseEntity<>(msg, status);
    }
}
