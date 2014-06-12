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

    void activate(long userId);

    void deactivate(long userId);

    void create(CreateEditCustomerDTO dto);

    void update(CreateEditCustomerDTO dto);

    void delete(long userId);

}
