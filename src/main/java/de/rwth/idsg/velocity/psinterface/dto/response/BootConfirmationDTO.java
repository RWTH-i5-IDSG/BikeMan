package de.rwth.idsg.velocity.psinterface.dto.response;

import lombok.Data;
import org.joda.time.LocalDateTime;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class BootConfirmationDTO {
    private LocalDateTime timestamp;
    private Integer heartbeatInterval;
}
