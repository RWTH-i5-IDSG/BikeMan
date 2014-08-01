package de.rwth.idsg.velocity.psinterface.dto.request;

import de.rwth.idsg.velocity.psinterface.dto.*;
import de.rwth.idsg.velocity.psinterface.dto.Error;
import lombok.Data;
import org.joda.time.LocalDateTime;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class PedelecStatusDTO {
    private String pedelecmanufacturerId;
    private Error pedelecErrorCode;
    private String pedelecInfo;
    private OperationState pedelecState;
    private LocalDateTime timestamp;
}
