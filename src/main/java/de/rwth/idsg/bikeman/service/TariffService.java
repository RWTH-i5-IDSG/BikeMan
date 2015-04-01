package de.rwth.idsg.bikeman.service;

import de.rwth.idsg.bikeman.domain.Transaction;
import de.rwth.idsg.bikeman.service.tariffPriceCalculations.Ticket2000Impl;
import de.rwth.idsg.bikeman.service.tariffPriceCalculations.Ticket3000Impl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

/**
 * Created by swam on 20/01/15.
 */

@Service
@Slf4j
public class TariffService {

    private TariffPriceCalculation ticket2000;
    private TariffPriceCalculation ticket3000;

    @PostConstruct
    public void init() {
        ticket2000 = new Ticket2000Impl();
        ticket3000 = new Ticket3000Impl();

    }

    public BigDecimal calculatePrice(Transaction transaction) {

        if (transaction == null || transaction.getStartDateTime() == null || transaction.getEndDateTime() == null)
            return BigDecimal.ZERO;

        switch (transaction.getBookedTariff().getName()) {
            case Ticket2000:
                return ticket2000.calculate(transaction);

            case Ticket3000:
                return ticket3000.calculate(transaction);

            default:
                break;
        }

        return BigDecimal.ZERO;

    }
}
