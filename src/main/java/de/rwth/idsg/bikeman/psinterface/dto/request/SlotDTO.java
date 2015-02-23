package de.rwth.idsg.bikeman.psinterface.dto.request;

import de.rwth.idsg.bikeman.psinterface.dto.OperationState;
import lombok.Data;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class SlotDTO {

    // used in boot notification
    private String slotManufacturerId;
    private Integer slotPosition;
    private String pedelecManufacturerId;

    // used in station status notification
    private String slotErrorCode;
    private String slotErrorInfo;
    private OperationState slotState;
}
