package de.rwth.idsg.velocity.psinterface.dto.request;

import lombok.Data;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class BatteryStatusDTO {
    private Float soc;
    private Float temperature;
    private Integer cycleCount;
    private Float voltage;
    private Float current;
}