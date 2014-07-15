package de.rwth.idsg.velocity.web.rest.dto.view;

import de.rwth.idsg.velocity.domain.OperationState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by swam on 23/05/14.
 */
@Getter
@ToString(includeFieldNames = true)
public class ViewStationSlotDTO {

    private Long stationSlotId;
    private String manufacturerId;
    private Integer slotPosition;
    private OperationState state;
    private Boolean isOccupied;
    private StationSlotPedelecDTO pedelec;

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
