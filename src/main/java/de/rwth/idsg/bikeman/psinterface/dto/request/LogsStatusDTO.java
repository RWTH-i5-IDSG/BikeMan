package de.rwth.idsg.bikeman.psinterface.dto.request;

import de.rwth.idsg.bikeman.psinterface.dto.LogsUpdateState;
import lombok.Data;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class LogsStatusDTO {
    private LogsUpdateState status;
}
