package de.rwth.idsg.bikeman.app.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ViewTariffPriceDTO {
    private Integer journeyTime;
    private Integer timeInterval;
    private BigDecimal rate;

    public ViewTariffPriceDTO(Integer journeyTime, Integer timeInterval, BigDecimal rate) {
        this.journeyTime = journeyTime;
        this.timeInterval = timeInterval;
        this.rate = rate;
    }
}