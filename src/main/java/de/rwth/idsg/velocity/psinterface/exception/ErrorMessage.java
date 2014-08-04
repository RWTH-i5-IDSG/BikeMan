package de.rwth.idsg.velocity.psinterface.exception;

import lombok.Data;

/**
 * Created by swam on 01/08/14.
 */

@Data
public class ErrorMessage {
    private final Long timestamp;
    private final String code;
    private final String message;
}
