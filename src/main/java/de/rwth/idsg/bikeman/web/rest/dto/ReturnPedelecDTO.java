package de.rwth.idsg.bikeman.web.rest.dto;

/**
 * Created by swam on 19/05/14.
 */
public class ReturnPedelecDTO {

    private long stationSlotId;

    private long pedelecId;

    public ReturnPedelecDTO(long stationSlotId, long pedelecId) {
        this.stationSlotId = stationSlotId;
        this.pedelecId = pedelecId;
    }

    protected  ReturnPedelecDTO() {

    }

    public long getStationSlotId() {
        return stationSlotId;
    }

    public void setStationSlotId(long stationSlotId) {
        this.stationSlotId = stationSlotId;
    }

    public long getPedelecId() {
        return pedelecId;
    }

    public void setPedelecId(long pedelecId) {
        this.pedelecId = pedelecId;
    }
}
