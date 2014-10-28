package de.rwth.idsg.bikeman.ixsi.dto.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * Created by max on 06/10/14.
 */
@AllArgsConstructor
@Getter
public class AvailabilityResponseDTO {

    private long pedelecId;
    private long stationId;
    private BigDecimal locationLatitude;
    private BigDecimal locationLongitude;
    private Float stateOfCharge;
//    private Float drivingRange;

}
