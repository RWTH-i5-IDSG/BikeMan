package de.rwth.idsg.bikeman.app.exception;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 23.02.2015
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@RequiredArgsConstructor
public class AppExceptionMessage {
    private final long timestamp;
    private final String code;
    private final String message;

    /**
     * This field is optional only for validation exceptions.
     * It contains a list of error messages for each failed field.
     */
    private List<String> fieldErrors;
}
