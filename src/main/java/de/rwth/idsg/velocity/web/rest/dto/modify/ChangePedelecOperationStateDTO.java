package de.rwth.idsg.velocity.web.rest.dto.modify;

import de.rwth.idsg.velocity.domain.OperationState;
import lombok.Data;

/**
 * Created by swam on 08/08/14.
 */

@Data
public class ChangePedelecOperationStateDTO {

    private Integer slotPosition;
//    private String pedelecManufacturerId;
    private OperationState pedelecState;
}
