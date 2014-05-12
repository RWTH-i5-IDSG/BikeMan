package de.rwth.idsg.velocity.web.rest.dto;

/**
 * Created by max on 08/05/14.
 */
public class PedelecDTO {

    private Boolean isAvailable;

    private Float stateOfCharge;

    private String pedelecId;

    public PedelecDTO() {
    }

    public PedelecDTO(String pedelecId, Float stateOfCharge, Boolean isAvailable) {
        this.pedelecId = pedelecId;
        this.stateOfCharge = stateOfCharge;
        this.isAvailable = isAvailable;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
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
                "isAvailable=" + isAvailable +
                ", stateOfCharge=" + stateOfCharge +
                ", pedelecId='" + pedelecId + '\'' +
                '}';
    }
}
