package de.rwth.idsg.bikeman.web.rest.dto.modify;

import de.rwth.idsg.bikeman.domain.TariffType;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

/**
 * Created by swam on 21/10/14.
 */

@Data
public class CreateEditCardAccountDTO {

    private String login;

    @NotBlank
    private String cardId;

    // check if cardPin has only four digits, e.g., '0034'
    @NotBlank
    @Digits(integer = 4, fraction = 0)
    private String cardPin;
    
    @NotNull
    private TariffType tariff;
}
