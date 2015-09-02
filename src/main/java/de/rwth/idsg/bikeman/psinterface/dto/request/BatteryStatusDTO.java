package de.rwth.idsg.bikeman.psinterface.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class BatteryStatusDTO {
    @Range(min = 0, max = 101)
    private Double soc;

    @Range(min = -272, max = 99999999)
    private Double temperature;

    @Range(min = 0, max = 99999999)
    private Integer cycleCount;

    @Range(min = 0, max = 99999999)
    private Double voltage;

    @Range(min = 0, max = 99999999)
    private Double current;
}