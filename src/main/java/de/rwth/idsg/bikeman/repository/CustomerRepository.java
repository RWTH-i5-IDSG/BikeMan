package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditCustomerDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewCustomerDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;

import java.util.List;

/**
 * Spring Data JPA repository for the Customer entity.
 */
public interface CustomerRepository {

    /**
     * self-explanatory
     */
    List<ViewCustomerDTO> findAll() throws DatabaseException;

    /**
    * Two customers could have the same name, hence the list as return
    */
    List<ViewCustomerDTO> findbyName(String name) throws DatabaseException;

    /**
     * Login (aka email) field of each customer is unique
     */
    ViewCustomerDTO findbyLogin(String login) throws DatabaseException;

    void activate(long userId) throws DatabaseException;

    void deactivate(long userId) throws DatabaseException;

    void create(CreateEditCustomerDTO dto) throws DatabaseException;

    void update(CreateEditCustomerDTO dto) throws DatabaseException;

    void delete(long userId) throws DatabaseException;

}
