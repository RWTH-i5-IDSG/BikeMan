package de.rwth.idsg.bikeman.service.tariffPriceCalculations;

import de.rwth.idsg.bikeman.app.dto.ViewTariffPriceDTO;
import de.rwth.idsg.bikeman.domain.Transaction;
import de.rwth.idsg.bikeman.service.TariffPriceCalculation;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Wolfgang Kluth on 20/01/15.
 */

public class Ticket2000Impl implements TariffPriceCalculation {

    final BigDecimal pricePerTimeUnit = new BigDecimal("12.34");
    final BigDecimal timeUnitInMin = new BigDecimal("30");

    @Override
    public BigDecimal calculate(Transaction transaction) {
        Duration duration = new Duration(
                transaction.getStartDateTime().toDateTime(DateTimeZone.UTC),
                transaction.getEndDateTime().toDateTime(DateTimeZone.UTC)
        );
        
        BigDecimal durationInMin = new BigDecimal(duration.getStandardMinutes());
        return durationInMin.divide(timeUnitInMin, 0, RoundingMode.CEILING)
                            .multiply(pricePerTimeUnit);
    }


    @Override
    public List<ViewTariffPriceDTO> listPrice() {
        return Arrays.asList(
                new ViewTariffPriceDTO(-1, 0, new BigDecimal("12.34")),
                new ViewTariffPriceDTO(0, 30, new BigDecimal("12.34"))
        );
    }
}
