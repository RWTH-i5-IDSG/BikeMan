package de.rwth.idsg.bikeman.service;

import de.rwth.idsg.bikeman.app.dto.ViewTariffPriceDTO;
import de.rwth.idsg.bikeman.domain.Transaction;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by swam on 20/01/15.
 */
public interface TariffPriceCalculation {
    
    
    // calculates price in euro
    BigDecimal calculate(Transaction transaction);

    List<ViewTariffPriceDTO> listPrice();
}
