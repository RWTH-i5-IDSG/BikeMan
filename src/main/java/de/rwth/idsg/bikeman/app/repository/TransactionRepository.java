package de.rwth.idsg.bikeman.app.repository;

import de.rwth.idsg.bikeman.app.dto.ViewTransactionDTO;
import de.rwth.idsg.bikeman.domain.Customer;

import java.util.List;

public interface TransactionRepository {
    List<ViewTransactionDTO> findByCustomerPaginated(Customer customer, Integer page);
    List<ViewTransactionDTO> findOpenByCustomer(Customer customer);
    Long numberOfOpenTransactionsByCustomer(Customer customer);
}
