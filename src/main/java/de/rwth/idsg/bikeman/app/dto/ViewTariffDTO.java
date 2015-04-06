package de.rwth.idsg.bikeman.app.dto;

import de.rwth.idsg.bikeman.domain.TariffType;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString(includeFieldNames = true)
public class ViewTariffDTO {

    private Long tariffId;
    private TariffType name;
    private String description;
    private Integer term;
    private BigDecimal periodicRate;

    public ViewTariffDTO (Long tariffId, TariffType name, String description,
                   Integer term, BigDecimal periodicRate)
    {
        this.tariffId = tariffId;
        this.name = name;
        this.description = description;
        this.term = term;
        this.periodicRate = periodicRate;
    }
}
