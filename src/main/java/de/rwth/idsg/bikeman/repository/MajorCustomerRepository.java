package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditMajorCustomerDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewMajorCustomerDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;

import java.util.List;

/**
 * Created by swam on 16/10/14.
 */

public interface MajorCustomerRepository {

    /**
     * self-explanatory
     */
    List<ViewMajorCustomerDTO> findAll() throws DatabaseException;

    ViewMajorCustomerDTO findOne(long majorCustomerId) throws DatabaseException;

    /**
     * Login (aka email) field of each majorcustomer is unique
     */
    ViewMajorCustomerDTO findByLogin(String login) throws DatabaseException;

    void create(CreateEditMajorCustomerDTO dto) throws DatabaseException;

    void update(CreateEditMajorCustomerDTO dto) throws DatabaseException;

    void delete(long userId) throws DatabaseException;
}
