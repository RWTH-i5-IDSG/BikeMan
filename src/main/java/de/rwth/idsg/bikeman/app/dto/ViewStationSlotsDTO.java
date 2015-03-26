package de.rwth.idsg.bikeman.app.dto;

import de.rwth.idsg.bikeman.domain.OperationState;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(includeFieldNames = true)
public class ViewStationSlotsDTO {

    private Long stationSlotId;
    private Integer stationSlotPosition;
    private OperationState state;
    private Boolean isOccupied;
    private Float stateOfCharge;

    public ViewStationSlotsDTO(Long stationSlotId, Integer stationSlotPosition,
                               OperationState state, Boolean isOccupied, Float stateOfCharge) {
        this.stationSlotId = stationSlotId;
        this.stationSlotPosition = stationSlotPosition;
        this.state = state;
        this.isOccupied = isOccupied;
        this.stateOfCharge = stateOfCharge;
    }
}
