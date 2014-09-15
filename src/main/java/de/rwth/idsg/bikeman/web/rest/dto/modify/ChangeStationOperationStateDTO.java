package de.rwth.idsg.bikeman.web.rest.dto.modify;

import de.rwth.idsg.bikeman.domain.OperationState;
import lombok.Data;import java.lang.Integer;

/**
 * Created by swam on 08/08/14.
 */

@Data
public class ChangeStationOperationStateDTO {
    private Integer slotPosition;
    private OperationState state;
}
