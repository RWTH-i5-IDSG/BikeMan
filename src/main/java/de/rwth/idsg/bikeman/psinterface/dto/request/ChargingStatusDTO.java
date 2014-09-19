package de.rwth.idsg.bikeman.psinterface.dto.request;

import de.rwth.idsg.bikeman.domain.ChargingState;
import lombok.Data;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class ChargingStatusDTO {
    private String pedelecManufacturerId;
    private String slotManufacturerId;
    private Long timestamp;
    private ChargingState chargingState;
    private Double meterValue;
    private BatteryStatusDTO battery;
}
