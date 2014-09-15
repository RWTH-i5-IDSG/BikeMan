package de.rwth.idsg.bikeman.psinterface.dto.request;

import lombok.Data;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class BatteryStatusDTO {
    private Double soc;
    private Double temperature;
    private Integer cycleCount;
    private Double voltage;
    private Double current;
}