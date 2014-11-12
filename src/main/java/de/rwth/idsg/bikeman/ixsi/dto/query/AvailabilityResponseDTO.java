package de.rwth.idsg.bikeman.ixsi.dto.query;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by max on 06/10/14.
 */
@Data
public class AvailabilityResponseDTO {

    private final String manufacturerId;
    private final String stationManufacturerId;
    private final BigDecimal locationLatitude;
    private final BigDecimal locationLongitude;
    private final Float stateOfCharge;
//    private Float drivingRange;

}
