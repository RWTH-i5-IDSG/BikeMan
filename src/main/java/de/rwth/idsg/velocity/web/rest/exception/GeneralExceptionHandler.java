package de.rwth.idsg.velocity.web.rest.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgokay on 14.07.14.
 */
@ControllerAdvice
@Slf4j
public class GeneralExceptionHandler {

//    private MessageSource messageSource;
//
//    @Autowired
//    public GeneralExceptionHandler(MessageSource messageSource) {
//        this.messageSource = messageSource;
//    }

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> processValidationException(MethodArgumentNotValidException e) {
        log.error("Exception happened", e);

        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : errors) {
            errorMessages.add(fieldError.getDefaultMessage());
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
