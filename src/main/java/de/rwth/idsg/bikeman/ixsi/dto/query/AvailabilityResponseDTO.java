package de.rwth.idsg.bikeman.ixsi.dto.query;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Created by max on 06/10/14.
 */
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class AvailabilityResponseDTO {

    private String manufacturerId;
    private String stationManufacturerId;
    private BigDecimal locationLatitude;
    private BigDecimal locationLongitude;
    private Double stateOfCharge;
//    private Float drivingRange;

    // For simple queries
    public AvailabilityResponseDTO(String manufacturerId, String stationManufacturerId, Double stateOfCharge) {
        this.manufacturerId = manufacturerId;
        this.stationManufacturerId = stationManufacturerId;
        this.stateOfCharge = stateOfCharge;
    }

    // For geo-location queries
    public AvailabilityResponseDTO(String manufacturerId, String stationManufacturerId,
                                   BigDecimal locationLatitude, BigDecimal locationLongitude, Double stateOfCharge) {
        this.manufacturerId = manufacturerId;
        this.stationManufacturerId = stationManufacturerId;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.stateOfCharge = stateOfCharge;
    }
}
