package de.rwth.idsg.bikeman.app.dto;

import lombok.*;

@Builder
@Getter
public class ViewPedelecSlotDTO {
    private Long stationSlotId;
    private Integer stationSlotPosition;
}
