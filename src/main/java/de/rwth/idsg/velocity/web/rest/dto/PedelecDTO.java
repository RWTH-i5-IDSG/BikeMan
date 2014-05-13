package de.rwth.idsg.velocity.web.rest.dto;

/**
 * Created by max on 08/05/14.
 */
public class PedelecDTO {

    private Boolean state;

    private Float stateOfCharge;

    private String pedelecId;

    public PedelecDTO() {
    }

    public PedelecDTO(String pedelecId, Float stateOfCharge, Boolean state) {
        this.pedelecId = pedelecId;
        this.stateOfCharge = stateOfCharge;
        this.state = state;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Float getStateOfCharge() {
        return stateOfCharge;
    }

    public void setStateOfCharge(Float stateOfCharge) {
        this.stateOfCharge = stateOfCharge;
    }

    public String getPedelecId() {
        return pedelecId;
    }

    public void setPedelecId(String pedelecId) {
        this.pedelecId = pedelecId;
    }

    @Override
    public String toString() {
        return "PedelecDTO{" +
                "state=" + state +
                ", stateOfCharge=" + stateOfCharge +
                ", pedelecId='" + pedelecId + '\'' +
                '}';
    }
}
