package de.rwth.idsg.bikeman.psinterface.dto.request;

import de.rwth.idsg.bikeman.domain.ChargingState;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class ChargingStatusDTO {
    @NotBlank
    private String pedelecManufacturerId;
    @NotBlank
    private Long timestamp;
    @NotBlank
    private ChargingState chargingState;
    @Range(min = 0, max = 99999999)
    private Double meterValue;
    private BatteryStatusDTO battery;
}
