package de.rwth.idsg.bikeman.app.dto;

import de.rwth.idsg.bikeman.domain.OperationState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class StationSlotsDTO {

    private Long stationSlotId;
    private Integer stationSlotPosition;
    private OperationState state;
    private Boolean isOccupied;
    private Double stateOfCharge;

}
