package de.rwth.idsg.bikeman.service.tariffPriceCalculations;

import de.rwth.idsg.bikeman.domain.Transaction;
import de.rwth.idsg.bikeman.service.TariffPriceCalculation;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;

/**
 * Created by Wolfgang Kluth on 20/01/15.
 */

// 20cent per minute
public class Ticket2000Impl implements TariffPriceCalculation {

    @Override
    public double calculate(Transaction transaction) {

        Duration duration = new Duration(
                transaction.getStartDateTime().toDateTime(DateTimeZone.UTC),
                transaction.getEndDateTime().toDateTime(DateTimeZone.UTC)
        );
        
        long durationInMin = duration.getStandardMinutes();
        

        double price = durationInMin * 0.20;
        
        return price;
    }
}
