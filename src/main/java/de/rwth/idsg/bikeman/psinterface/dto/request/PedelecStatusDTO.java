package de.rwth.idsg.bikeman.psinterface.dto.request;

import de.rwth.idsg.bikeman.psinterface.dto.OperationState;
import lombok.Data;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class PedelecStatusDTO {
    private String pedelecmanufacturerId;
    private String pedelecErrorCode;
    private String pedelecErrorInfo;
    private OperationState pedelecState;
    private Long timestamp;
}