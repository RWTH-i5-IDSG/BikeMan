package de.rwth.idsg.bikeman.service.tariffPriceCalculations;

import de.rwth.idsg.bikeman.domain.Transaction;
import de.rwth.idsg.bikeman.service.TariffPriceCalculation;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;

import java.math.BigDecimal;

/**
 * Created by Wolfgang Kluth on 20/01/15.
 */

// 20cent per minute
public class Ticket2000Impl implements TariffPriceCalculation {

    @Override
    public BigDecimal calculate(Transaction transaction) {
        final BigDecimal pricePerMin = new BigDecimal("0.20");

        Duration duration = new Duration(
                transaction.getStartDateTime().toDateTime(DateTimeZone.UTC),
                transaction.getEndDateTime().toDateTime(DateTimeZone.UTC)
        );
        
        BigDecimal durationInMin = new BigDecimal(duration.getStandardMinutes());
        BigDecimal price = durationInMin.multiply(pricePerMin);
        
        return price;
    }
}
