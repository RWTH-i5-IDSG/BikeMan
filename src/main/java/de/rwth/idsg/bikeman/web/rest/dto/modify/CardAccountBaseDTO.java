package de.rwth.idsg.bikeman.web.rest.dto.modify;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Digits;

/**
 * Created by Wolfgang Kluth on 23/02/16.
 */

@Data
public class CardAccountBaseDTO {

    @NotBlank
    private String cardId;

    // check if cardPin has only four digits, e.g., '0034'
    @NotBlank
    @Digits(integer = 4, fraction = 0)
    private String cardPin;

}
