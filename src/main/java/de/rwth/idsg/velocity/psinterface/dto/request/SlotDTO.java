package de.rwth.idsg.velocity.psinterface.dto.request;

import de.rwth.idsg.velocity.psinterface.dto.OperationState;
import lombok.Data;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class SlotDTO {
    private String slotManufacturerId;
    private Integer slotPosition;
    private String pedelecManufacturerId;
    private String slotErrorCode;
    private String slotInfo;
    private OperationState slotState;
}
