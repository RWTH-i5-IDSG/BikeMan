package de.rwth.idsg.bikeman.app.dto;


import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RentPedelecDTO {

    @NotNull
    private Long stationSlotId;

    @NotBlank
    private String cardPin;

}
