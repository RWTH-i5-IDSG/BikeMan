package de.rwth.idsg.velocity.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Created by max on 08/05/14.
 */
public class PedelecDTO {

    private String pedelecId;

    public PedelecDTO(String pedelecId, Float stateOfCharge, Boolean state) {
        this.pedelecId = pedelecId;
    }

    @JsonCreator
    public PedelecDTO() {
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
                ", pedelecId='" + pedelecId + '\'' +
                '}';
    }
}
