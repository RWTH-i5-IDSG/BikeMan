package de.rwth.idsg.velocity.psinterface.dto;

import lombok.Data;
import org.joda.time.LocalDateTime;

/**
 * Created by swam on 01/08/14.
 */

@Data
public class ErrorMessage {
    private LocalDateTime timestamp;
    private String code;
    private String message;
}
