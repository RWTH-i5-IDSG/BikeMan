package de.rwth.idsg.bikeman.service;

import de.rwth.idsg.bikeman.domain.Transaction;

/**
 * Created by swam on 20/01/15.
 */
public interface TariffPriceCalculation {
    
    
    // calculates price in euro
     double calculate(Transaction transaction);
}
