package de.rwth.idsg.bikeman.web.rest.dto.modify;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.rwth.idsg.bikeman.domain.TariffType;
import de.rwth.idsg.bikeman.web.rest.dto.util.CardAccountBatchDeserializer;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Wolfgang Kluth on 22/02/16.
 */

@Data
public class CreateCardAccountBatchDTO {

    private String login;

    @JsonDeserialize(using = CardAccountBatchDeserializer.class)
    private List<CardAccountBaseDTO> cardAccountBatch;

    @NotNull
    private TariffType tariff;
}
