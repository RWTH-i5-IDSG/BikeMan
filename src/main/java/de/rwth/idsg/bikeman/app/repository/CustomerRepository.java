package de.rwth.idsg.bikeman.app.repository;

import de.rwth.idsg.bikeman.app.dto.CreateCustomerDTO;
import de.rwth.idsg.bikeman.app.dto.ViewCustomerDTO;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.domain.Customer;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;

import java.util.Optional;


public interface CustomerRepository {

    ViewCustomerDTO findOne(Long customerId) throws AppException;
    Optional<Customer> findByLogin(String login) throws DatabaseException;
    CreateCustomerDTO create(CreateCustomerDTO dto) throws AppException;

}
