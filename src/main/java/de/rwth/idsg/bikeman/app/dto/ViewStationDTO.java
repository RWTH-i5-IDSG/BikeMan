package de.rwth.idsg.bikeman.app.dto;

import de.rwth.idsg.bikeman.domain.OperationState;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString(includeFieldNames = true)
public class ViewStationDTO {

    private Long stationId;
    private String name;
    private BigDecimal locationLatitude;
    private BigDecimal locationLongitude;
    private OperationState state;
    private String note;
    private Long numFreeSlots;
    private Long numAllSlots;

    public ViewStationDTO(Long stationId, String name,
                          BigDecimal locationLatitude, BigDecimal locationLongitude, OperationState state,
                          String note, Long numFreeSlots, Long numAllSlots) {
        this.stationId = stationId;
        this.name = name;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.state = state;
        this.note = note;
        this.numFreeSlots = numFreeSlots;
        this.numAllSlots = numAllSlots;
    }
}
