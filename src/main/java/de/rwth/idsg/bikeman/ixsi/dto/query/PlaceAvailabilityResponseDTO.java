package de.rwth.idsg.bikeman.ixsi.dto.query;

import lombok.Data;

/**
 * Created by max on 06/10/14.
 */
@Data
public class PlaceAvailabilityResponseDTO {

    private final long stationId;
    private final Integer availableSlots;
//    private int availablePedelecs;

}
