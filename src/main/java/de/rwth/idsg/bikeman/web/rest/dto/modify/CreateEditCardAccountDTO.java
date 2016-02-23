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
public class CreateEditCardAccountDTO extends CardAccountBaseDTO {

    private String login;
    
    @NotNull
    private TariffType tariff;
}
