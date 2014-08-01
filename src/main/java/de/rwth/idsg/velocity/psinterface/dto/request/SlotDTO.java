package de.rwth.idsg.velocity.psinterface.dto.request;

import de.rwth.idsg.velocity.psinterface.dto.*;
import de.rwth.idsg.velocity.psinterface.dto.Error;
import lombok.Data;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class SlotDTO {
    private String slotManufacturerId;
    private Integer slotPosition;
    private String pedelecManufacturerId;
    private Error slotErrorCode;
    private String slotInfo;
    private OperationState slotState;
}
