package de.rwth.idsg.bikeman.app.repository;

import de.rwth.idsg.bikeman.app.dto.ViewCustomerDTO;
import de.rwth.idsg.bikeman.app.exception.AppException;


public interface CustomerRepository {

    ViewCustomerDTO findOne(Long customerId) throws AppException;

}
