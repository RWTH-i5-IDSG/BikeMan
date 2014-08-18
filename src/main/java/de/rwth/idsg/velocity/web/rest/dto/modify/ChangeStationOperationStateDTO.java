package de.rwth.idsg.velocity.web.rest.dto.modify;

import de.rwth.idsg.velocity.domain.OperationState;
import lombok.Data;import java.lang.Integer;import java.lang.String;

/**
 * Created by swam on 08/08/14.
 */

@Data
public class ChangeStationOperationStateDTO {
    private Integer slotPosition;
    private OperationState state;
}
