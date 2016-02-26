package de.rwth.idsg.bikeman.ixsi.dto;

import lombok.Data;

/**
 * Created by max on 06/10/14.
 */
@Data
public class PlaceAvailabilityResponseDTO {

    private final String manufacturerId;
    private final Integer availableSlots;
//    private int availablePedelecs;

}
