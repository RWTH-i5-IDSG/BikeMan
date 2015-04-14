package de.rwth.idsg.bikeman.app.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(includeFieldNames = true)
public class PedelecRentalDTO {
    private Long stationSlotId;
}
