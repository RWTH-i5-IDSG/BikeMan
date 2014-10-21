package de.rwth.idsg.bikeman.web.rest.dto.modify;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Digits;

/**
 * Created by swam on 21/10/14.
 */

@Data
public class CreateEditCardAccountDTO {

    @NotEmpty
    private String cardId;

    // check if cardPin has only four digits, e.g., '0034'
    @NotEmpty
    @Digits(integer = 4, fraction = 0)
    private String cardPin;
}