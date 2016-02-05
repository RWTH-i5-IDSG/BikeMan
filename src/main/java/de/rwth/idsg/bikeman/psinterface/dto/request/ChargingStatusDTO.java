package de.rwth.idsg.bikeman.psinterface.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.rwth.idsg.bikeman.domain.ChargingState;
import de.rwth.idsg.bikeman.psinterface.CustomDoubleDeserializer;
import de.rwth.idsg.bikeman.psinterface.UnixTimestampDeserializer;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.joda.time.DateTime;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class ChargingStatusDTO {

    @NotBlank
    private String pedelecManufacturerId;

    @NotBlank
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    private DateTime timestamp;

    @NotBlank
    private ChargingState chargingState;

    @Range(min = 0, max = 99999999)
    @JsonDeserialize(using = CustomDoubleDeserializer.class)
    private Double meterValue;

    private BatteryStatusDTO battery;
}
