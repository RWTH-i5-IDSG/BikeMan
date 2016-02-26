package de.rwth.idsg.bikeman.app.dto;

import com.fasterxml.jackson.annotation.JsonView;
import de.rwth.idsg.bikeman.domain.TariffType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString(includeFieldNames = true)
public class ViewTariffDTO {

    public interface ListView {};

    @JsonView(ListView.class)
    private Long tariffId;

    @JsonView(ListView.class)
    private TariffType name;

    @JsonView(ListView.class)
    private String description;

    @JsonView(ListView.class)
    private Integer term;

    @JsonView(ListView.class)
    private BigDecimal periodicRate;

    private List<ViewTariffPriceDTO> priceList;

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
