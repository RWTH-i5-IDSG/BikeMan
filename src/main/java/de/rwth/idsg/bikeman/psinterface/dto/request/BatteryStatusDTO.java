package de.rwth.idsg.bikeman.psinterface.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.rwth.idsg.bikeman.psinterface.CustomDoubleDeserializer;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class BatteryStatusDTO {

    @Range(min = 0, max = 101)
    @JsonDeserialize(using = CustomDoubleDeserializer.class)
    private Double soc;

    @Range(min = -272, max = 99999999)
    @JsonDeserialize(using = CustomDoubleDeserializer.class)
    private Double temperature;

    @Range(min = 0, max = 99999999)
    private Integer cycleCount;

    @Range(min = 0, max = 99999999)
    @JsonDeserialize(using = CustomDoubleDeserializer.class)
    private Double voltage;

    @Range(min = 0, max = 99999999)
    @JsonDeserialize(using = CustomDoubleDeserializer.class)
    private Double current;
}