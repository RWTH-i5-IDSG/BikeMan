package de.rwth.idsg.velocity.endpoint;

import de.rwth.idsg.velocity.domain.Station;
import de.rwth.idsg.velocity.domain.StationSlot;

/**
 * Created by swam on 16/05/14.
 */
public class StationEndpoint {

    private Station station;

    public StationEndpoint(Station station) {
        this.station = station;
    }

    public boolean openSlot(StationSlot stationSlot) {

        //TODO: implement functionality, maybe stationStub for different Station integrations

        return true;
    }

    public boolean slotIsEngadged(StationSlot stationSlot) {

        //TODO: implement functionality, maybe stationStub for different Station integrations

        return false;
    }
}
