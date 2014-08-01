package de.rwth.idsg.velocity.psinterface.dto.request;

import de.rwth.idsg.velocity.psinterface.dto.LogsUpdateState;
import lombok.Data;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class LogsStatusDTO {
    private LogsUpdateState status;
}
