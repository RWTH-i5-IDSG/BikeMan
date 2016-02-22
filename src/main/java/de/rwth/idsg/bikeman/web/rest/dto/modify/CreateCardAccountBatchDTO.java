package de.rwth.idsg.bikeman.web.rest.dto.modify;

import de.rwth.idsg.bikeman.domain.TariffType;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by Wolfgang Kluth on 22/02/16.
 */

@Data
public class CreateCardAccountBatchDTO {

    private String login;

    // todo: check if valid/ right format!
    @NotBlank
    private String batch;

    @NotNull
    private TariffType tariff;
}
