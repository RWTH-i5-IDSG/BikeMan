package de.rwth.idsg.velocity.psinterface.dto.response;

import lombok.*;
import org.joda.time.LocalDateTime;

import java.io.Serializable;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class BootConfirmationDTO implements Serializable {
    private Long timestamp;
    private Integer heartbeatInterval;
}
