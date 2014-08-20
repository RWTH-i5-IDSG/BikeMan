package de.rwth.idsg.velocity.psinterface.dto.request;

import de.rwth.idsg.velocity.psinterface.dto.OperationState;
import lombok.Data;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class PedelecStatusDTO {
    private String pedelecmanufacturerId;
    private String pedelecErrorCode;
    private String pedelecInfo;
    private OperationState pedelecState;
    private Long timestamp;
}
