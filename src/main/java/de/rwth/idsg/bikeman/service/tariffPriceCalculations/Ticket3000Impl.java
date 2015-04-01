package de.rwth.idsg.bikeman.service.tariffPriceCalculations;

import de.rwth.idsg.bikeman.domain.Transaction;
import de.rwth.idsg.bikeman.service.TariffPriceCalculation;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;

import java.math.BigDecimal;

/**
 * Created by Wolfgang Kluth on 20/01/15.
 */

// 50cent per minute and first 30min are free
public class Ticket3000Impl implements TariffPriceCalculation {

    @Override
    public BigDecimal calculate(Transaction transaction) {
        final BigDecimal pricePerMin = new BigDecimal("0.50");
        final BigDecimal freeTimeInMin = new BigDecimal(30);

        Duration duration = new Duration(
                transaction.getStartDateTime().toDateTime(DateTimeZone.UTC),
                transaction.getEndDateTime().toDateTime(DateTimeZone.UTC)
        );

        BigDecimal durationInMin = new BigDecimal(duration.getStandardMinutes());

        if (durationInMin.compareTo(freeTimeInMin) != 1) {
            return BigDecimal.ZERO;
        }
        
        // 50cent per minute after free 30min
        BigDecimal price = (durationInMin.subtract(freeTimeInMin)).multiply(pricePerMin);

        return price;
    }
}
