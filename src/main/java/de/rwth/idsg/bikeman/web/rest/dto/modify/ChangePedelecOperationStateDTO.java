package de.rwth.idsg.bikeman.web.rest.dto.modify;

import de.rwth.idsg.bikeman.domain.OperationState;
import lombok.Data;

/**
 * Created by swam on 08/08/14.
 */

@Data
public class ChangePedelecOperationStateDTO {

    private Integer slotPosition;
    private OperationState pedelecState;
}
