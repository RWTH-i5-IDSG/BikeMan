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

public class Ticket3000Impl implements TariffPriceCalculation {

    @Override
    public BigDecimal calculate(Transaction transaction) {
        final BigDecimal pricePerTimeUnit = new BigDecimal("12.34");
        final BigDecimal timeUnitInMin = new BigDecimal("30");
        final BigDecimal freeTimeInMin = new BigDecimal("30");

        Duration duration = new Duration(
                transaction.getStartDateTime().toDateTime(DateTimeZone.UTC),
                transaction.getEndDateTime().toDateTime(DateTimeZone.UTC)
        );

        BigDecimal durationInMin = new BigDecimal(duration.getStandardMinutes());

        if (durationInMin.compareTo(freeTimeInMin) != 1) {
            return BigDecimal.ZERO;
        }

        BigDecimal price = (durationInMin
                            .subtract(freeTimeInMin))
                            .divide(timeUnitInMin, 0, RoundingMode.CEILING)
                            .multiply(pricePerTimeUnit);

        return price;
    }

    @Override
    public List<ViewTariffPriceDTO> listPrice() {
        return Arrays.asList(
                new ViewTariffPriceDTO(-1, 0, new BigDecimal("0")),
                new ViewTariffPriceDTO(0, 30, new BigDecimal("12.34"))
        );
    }

}
