package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.Address;
import de.rwth.idsg.velocity.domain.Customer;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditCustomerDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewCustomerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by sgokay on 05.06.14.
 */
@Repository
@Transactional
public class CustomerRepositoryImpl implements CustomerRepository {

    private static final Logger log = LoggerFactory.getLogger(CustomerRepositoryImpl.class);

    @PersistenceContext
    EntityManager em;

    @Override
    public List<ViewCustomerDTO> findAll() {
        // TODO
        return null;
    }

    @Override
    public List<ViewCustomerDTO> findbyName(String firstName, String lastName) {
        // TODO
        return null;
    }

    @Override
    public ViewCustomerDTO findbyEmail(String eMailAddress) {
        // TODO
        return null;
    }

    @Override
    public ViewCustomerDTO findbyLogin(String login) {
        // TODO
        return null;
    }

    @Override
    public void create(CreateEditCustomerDTO dto) {
        Customer customer = new Customer();
        setFields(customer, dto);
        em.persist(customer);
        log.debug("Created new customer {}", customer);
    }

    @Override
    public void update(CreateEditCustomerDTO dto) {
        final String login = dto.getLogin();
        if (login == null) {
            return;
        }

        Customer customer = em.find(Customer.class, login);
        if (customer == null) {
            log.error("No customer with login: {} to update.", login);
        } else {
            setFields(customer, dto);
            em.merge(customer);
            log.debug("Updated customer {}", customer);
        }
    }

    @Override
    public void delete(String login) {
        Customer customer = em.find(Customer.class, login);
        if (customer == null) {
            log.error("No customer with login: {} to delete.", login);
        } else {
            em.remove(customer);
            log.debug("Deleted customer {}", customer);
        }
    }

    /**
     * This method sets the fields of the customer to the values in DTO.
     *
     * Important: The LOGIN field is not set!
     */
    private void setFields(Customer customer, CreateEditCustomerDTO dto) {
        customer.setCustomerId(dto.getCustomerId());
        customer.setCardId(dto.getCustomerId());
        customer.setFirstname(dto.getFirstname());
        customer.setLastname(dto.getLastname());
        customer.setBirthday(dto.getBirthday());
        customer.setMailAddress(dto.getMailAddress());
        customer.setIsActivated(dto.getIsActivated());

        // for create (brand new address entity)
        if (customer.getAddress() == null) {
            customer.setAddress(dto.getAddress());

        // for edit (keep the address ID)
        } else {
            Address add = customer.getAddress();
            Address dtoAdd = dto.getAddress();
            add.setStreetAndHousenumber(dtoAdd.getStreetAndHousenumber());
            add.setZip(dtoAdd.getZip());
            add.setCity(dtoAdd.getCity());
            add.setCountry(dtoAdd.getCountry());
        }
    }
}
