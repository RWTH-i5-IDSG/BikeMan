package de.rwth.idsg.velocity.web.rest.dto;

import de.rwth.idsg.velocity.domain.PedelecState;
import de.rwth.idsg.velocity.domain.StationSlot;
import lombok.Getter;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Created by sgokay on 20.05.14.
 */
public class ViewPedelecDTO {

    @Getter private String pedelecId;
    @Getter private Float stateOfCharge;
    @Getter private PedelecState state;
    @Getter private StationSlot stationSlot;

    // This is only temporary, until stationslot problem is resolved
    public ViewPedelecDTO(String pedelecId, Float stateOfCharge, PedelecState state) {
        this.pedelecId = pedelecId;
        this.stateOfCharge = stateOfCharge;
        this.state = state;
    }

    public ViewPedelecDTO(String pedelecId, Float stateOfCharge, PedelecState state, StationSlot stationSlot) {
        this.pedelecId = pedelecId;
        this.stateOfCharge = stateOfCharge;
        this.state = state;
        this.stationSlot = stationSlot;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("pedelecId", pedelecId)
                .append("stateOfCharge", stateOfCharge)
                .append("state", state)
                .append("stationSlot", stationSlot)
                .toString();
    }
}
