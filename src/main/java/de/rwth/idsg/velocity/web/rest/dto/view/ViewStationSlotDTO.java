package de.rwth.idsg.velocity.web.rest.dto.view;

import de.rwth.idsg.velocity.domain.OperationState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by swam on 23/05/14.
 */
@ToString(includeFieldNames = true)
public class ViewStationSlotDTO {

    @Getter private Long stationSlotId;
    @Getter private String manufacturerId;
    @Getter private Integer slotPosition;
    @Getter private OperationState state;
    @Getter private Boolean isOccupied;
    @Getter private StationSlotPedelecDTO pedelec;

    public ViewStationSlotDTO(Long stationSlotId, String manufacturerId, Integer slotPosition,
                              OperationState state, Boolean isOccupied, Long pedelecId,
                              String pedelecManufacturerId) {
        this.stationSlotId = stationSlotId;
        this.manufacturerId = manufacturerId;
        this.slotPosition = slotPosition;
        this.state = state;
        this.isOccupied = isOccupied;

        if (isOccupied) {
            this.pedelec = new StationSlotPedelecDTO(pedelecId, pedelecManufacturerId);
        }
    }

    @Data
    class StationSlotPedelecDTO {
        private final Long pedelecId;
        private final String manufacturerId;
    }
}
