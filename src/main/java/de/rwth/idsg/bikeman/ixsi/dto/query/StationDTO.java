package de.rwth.idsg.bikeman.ixsi.dto.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * Created by max on 01/10/14.
 */
@AllArgsConstructor
@Getter
public class StationDTO {
    private String stationId;
    private BigDecimal location_longitude;
    private BigDecimal location_latitude;
    private int slotCount;
    private String name;
    private String providerId;
    private String description;
}
