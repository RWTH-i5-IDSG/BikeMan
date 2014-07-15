package de.rwth.idsg.velocity.web.rest.exception;

import lombok.extern.slf4j.Slf4j;
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

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by sgokay on 14.07.14.
 */
@ControllerAdvice
@Slf4j
public class GeneralExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public void processException(HttpServletResponse response, Exception e) {
        log.error("Exception happened", e);
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ErrorMessage> processDatabaseException(DatabaseException e) {
        log.error("Exception happened", e);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorMessage msg = new ErrorMessage(
                status.value(),
                status.getReasonPhrase(),
                e.getMessage()
        );
        return new ResponseEntity<>(msg, status);
    }

    /*
    * Catches the controller path errors of the Rest API.
    *
    * Example: If a controller declares an integer in the path, but frontend sends anything but an integer.
    */
    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ErrorMessage> processTypeException(TypeMismatchException e) {
        log.error("Exception happened", e);

        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ErrorMessage msg = new ErrorMessage(
                status.value(),
                status.getReasonPhrase(),
                e.getMessage()
        );
        return new ResponseEntity<>(msg, status);
    }

    /*
    * Catches the Hibernate validation errors and
    * responds with an error message in appropriate language.
    */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> processValidationException(MethodArgumentNotValidException e) {
        log.error("Exception happened", e);

        Locale currentLocale =  LocaleContextHolder.getLocale();

        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : errors) {
            String localizedError = messageSource.getMessage(fieldError, currentLocale);
            errorMessages.add(localizedError);
        }

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorMessage msg = new ErrorMessage(
                status.value(),
                status.getReasonPhrase(),
                "Validation failed for the submitted form"
        );
        msg.setFieldErrors(errorMessages);
        return new ResponseEntity<>(msg, status);
    }
}
