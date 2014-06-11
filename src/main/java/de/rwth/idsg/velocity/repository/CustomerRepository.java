package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditCustomerDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewCustomerDTO;

import java.util.List;

/**
 * Spring Data JPA repository for the Customer entity.
 */
public interface CustomerRepository {

    /**
     * self-explanatory
     */
    List<ViewCustomerDTO> findAll();

    /**
    * Two customers could have the same full name, hence the list as return
    */
    List<ViewCustomerDTO> findbyName(String firstName, String lastName);

    /**
     * Login (aka email) field of each customer is unique
     */
    ViewCustomerDTO findbyLogin(String login);

    /**
     * self-explanatory
     */
    void create(CreateEditCustomerDTO dto);

    /**
     * self-explanatory
     */
    void update(CreateEditCustomerDTO dto);

    /**
     * self-explanatory
     */
    void delete(long userId);

}
