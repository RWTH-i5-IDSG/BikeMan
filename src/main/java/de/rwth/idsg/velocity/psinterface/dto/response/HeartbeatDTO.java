package de.rwth.idsg.velocity.psinterface.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.joda.time.LocalDateTime;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class HeartbeatDTO {
    private Long timestamp;
}
