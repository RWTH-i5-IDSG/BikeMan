package de.rwth.idsg.velocity.web.rest.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * Default error object that is returned when an exception is thrown.
 *
 * Created by sgokay on 14.07.14.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessage {

    private final int status;
    private final String error;
    private final String message;

    /**
     * This field is optional only for validation exceptions.
     * It contains a list of error messages for each failed field.
     */
    private List<String> fieldErrors;
}