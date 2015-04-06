package de.rwth.idsg.bikeman.app.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.LocalDateTime;

@Getter
@Setter
@ToString(includeFieldNames = true)
public class ChangeTariffDTO {
    private Long tariffId;
    //private Boolean automaticRenewal;
    //private LocalDateTime changeDateTime;

    /*public ChangeTariffDTO(Long tariffId, Boolean automaticRenewal) {
        this.tariffId = tariffId;
        this.automaticRenewal = automaticRenewal;
    }

    public ChangeTariffDTO(Long tariffId, Boolean automaticRenewal, LocalDateTime changeDateTime) {
        this.tariffId = tariffId;
        this.automaticRenewal = automaticRenewal;
        this.changeDateTime = changeDateTime;
    }

    public ChangeTariffDTO(LocalDateTime changeDateTime) {
        this.changeDateTime = changeDateTime;
    }*/
}
