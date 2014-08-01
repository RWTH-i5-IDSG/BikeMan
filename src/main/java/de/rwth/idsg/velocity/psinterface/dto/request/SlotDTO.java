package de.rwth.idsg.velocity.psinterface.dto.request;

import de.rwth.idsg.velocity.psinterface.dto.*;
import de.rwth.idsg.velocity.psinterface.dto.ErrorMessage;
import lombok.Data;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class SlotDTO {
    private String slotManufacturerId;
    private Integer slotPosition;
    private String pedelecManufacturerId;
    private ErrorMessage slotErrorCode;
    private String slotInfo;
    private OperationState slotState;
}
