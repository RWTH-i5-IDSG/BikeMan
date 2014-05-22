package de.rwth.idsg.velocity.web.rest.dto.view;

import de.rwth.idsg.velocity.domain.OperationState;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by sgokay on 20.05.14.
 */
@ToString(includeFieldNames = true)
public class ViewPedelecDTO {

    @Getter private Long pedelecId;
    @Getter private String manufacturerId;
    @Getter private Float stateOfCharge;
    @Getter private OperationState state;
    @Getter private ViewStationSlotDTO stationSlot;

    public ViewPedelecDTO(Long pedelecId, String manufacturerId, Float stateOfCharge,
                          OperationState state, Long stationSlotId, Long stationId, Boolean stationSlotState) {
        this.pedelecId = pedelecId;
        this.manufacturerId = manufacturerId;
        this.stateOfCharge = stateOfCharge;
        this.state = state;
        this.stationSlot = new ViewStationSlotDTO(stationSlotId, stationId, stationSlotState);
    }

    @ToString(callSuper = true, includeFieldNames = true)
    class ViewStationSlotDTO {

        @Getter private Long id;
        @Getter private Long stationId;
        @Getter private Boolean state;

        ViewStationSlotDTO(Long id, Long stationId, Boolean state) {
            this.id = id;
            this.stationId = stationId;
            this.state = state;
        }
    }
}
