package de.rwth.idsg.bikeman.service.tariffPriceCalculations;

import de.rwth.idsg.bikeman.domain.Transaction;
import de.rwth.idsg.bikeman.service.TariffPriceCalculation;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;

/**
 * Created by Wolfgang Kluth on 20/01/15.
 */

// 50cent per minute and first 30min are free
public class Ticket3000Impl implements TariffPriceCalculation {

    @Override
    public double calculate(Transaction transaction) {

        Duration duration = new Duration(
                transaction.getStartDateTime().toDateTime(DateTimeZone.UTC),
                transaction.getEndDateTime().toDateTime(DateTimeZone.UTC)
        );

        long durationInMin = duration.getStandardMinutes();

        if (durationInMin <= 30) {
            return 0;
        }
        
        // 50cent per minute after free 30min
        double price = (durationInMin-30) * 0.50;

        return price;
    }
}
