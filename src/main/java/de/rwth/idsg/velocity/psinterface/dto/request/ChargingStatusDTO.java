package de.rwth.idsg.velocity.psinterface.dto.request;

import de.rwth.idsg.velocity.domain.ChargingState;
import de.rwth.idsg.velocity.psinterface.dto.OperationState;
import lombok.Data;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class ChargingStatusDTO {
    private String pedelecManufacturerId;
    private String slotManufacturerId;
    private Long timestamp;
    private ChargingState charginState; //?
    private Double meterValue;
    private BatteryStatusDTO battery;
}
