package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.Address;
import de.rwth.idsg.velocity.domain.Customer;
import de.rwth.idsg.velocity.domain.login.Authority;
import de.rwth.idsg.velocity.security.AuthoritiesConstants;
import de.rwth.idsg.velocity.web.rest.BackendException;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditCustomerDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewCustomerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;

/**
 * Created by sgokay on 05.06.14.
 */
@Repository
@Transactional
public class CustomerRepositoryImpl implements CustomerRepository {

    private static final Logger log = LoggerFactory.getLogger(CustomerRepositoryImpl.class);

    private enum Operation { CREATE, UPDATE };
    private enum FindType { ALL, BY_NAME, BY_LOGIN };

    @PersistenceContext
    EntityManager em;

    @Override
    public List<ViewCustomerDTO> findAll() throws BackendException {
        try {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            return em.createQuery(
                    getQuery(builder, FindType.ALL, null, null, null)
            ).getResultList();

        } catch (Exception e) {
            log.error("Exception happened: {}", e);
            throw new BackendException("Failed during database operation.");
        }
    }

    @Override
    public List<ViewCustomerDTO> findbyName(String firstname, String lastname) throws BackendException {
        List<ViewCustomerDTO> list = null;
        try {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            list = em.createQuery(
                    getQuery(builder, FindType.BY_NAME, firstname, lastname, null)
            ).getResultList();

        } catch (Exception e) {
            log.error("Exception happened: {}", e);
            throw new BackendException("Failed during database operation.");
        }

        if (list.isEmpty()) {
            throw new BackendException("No customer found with name " + firstname + " " + lastname);
        } else {
            return list;
        }
    }

    @Override
    public ViewCustomerDTO findbyLogin(String login) throws BackendException {
        try {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            return em.createQuery(
                    getQuery(builder, FindType.BY_LOGIN, null, null, login)
            ).getSingleResult();

        } catch (NoResultException e) {
            log.error("Exception happened: {}", e);
            throw new BackendException("No customer found with login " + login);

        } catch (Exception e) {
            log.error("Exception happened: {}", e);
            throw new BackendException("Failed during database operation.");
        }
    }

    @Override
    public void activate(long userId) throws BackendException {
        Customer customer = getCustomerEntity(userId);
        try {
            customer.setIsActivated(true);
            em.merge(customer);
            log.debug("Activated customer {}", customer);

        } catch (Exception e) {
            log.error("Exception happened: {}", e);
            throw new BackendException("Failed to activate customer with userId " + userId);
        }
    }

    @Override
    public void deactivate(long userId) throws BackendException {
        Customer customer = getCustomerEntity(userId);
        try {
            customer.setIsActivated(false);
            em.merge(customer);
            log.debug("Deactivated customer {}", customer);

        } catch (Exception e) {
            log.error("Exception happened: {}", e);
            throw new BackendException("Failed to deactivate customer with userId " + userId);
        }
    }

    @Override
    public void create(CreateEditCustomerDTO dto) throws BackendException {
        Customer customer = new Customer();
        setFields(customer, dto, Operation.CREATE);

        try {
            em.persist(customer);
            log.debug("Created new customer {}", customer);

        } catch (EntityExistsException e) {
            log.error("Exception happened: {}", e);
            throw new BackendException("This customer exists already.");

        } catch (Exception e) {
            log.error("Exception happened: {}", e);
            throw new BackendException("Failed to create a new customer.");
        }
    }

    @Override
    public void update(CreateEditCustomerDTO dto) throws BackendException {
        final Long userId = dto.getUserId();
        if (userId == null) {
            return;
        }

        Customer customer = getCustomerEntity(userId);
        try {
            setFields(customer, dto, Operation.UPDATE);
            em.merge(customer);
            log.debug("Updated customer {}", customer);

        } catch (Exception e) {
            log.error("Exception happened: {}", e);
            throw new BackendException("Failed to update customer with userId " + userId);
        }
    }

    @Override
    public void delete(long userId) throws BackendException {
        Customer customer = getCustomerEntity(userId);
        try {
            em.remove(customer);
            log.debug("Deleted customer {}", customer);

        } catch (Exception e) {
            log.error("Exception happened: {}", e);
            throw new BackendException("Failed to delete customer with userId " + userId);
        }
    }

    /**
     * Returns a customer, or throws exception when no customer exists.
     *
     */
    private Customer getCustomerEntity(long userId) throws BackendException {
        Customer customer = em.find(Customer.class, userId);
        if (customer == null) {
            throw new BackendException("No customer with userId " + userId);
        } else {
            return customer;
        }
    }

    /**
     * This method sets the fields of the customer to the values in DTO.
     *
     */
    private void setFields(Customer customer, CreateEditCustomerDTO dto, Operation operation) {

        // TODO: Should the login be changeable? Who sets the field? Clarify!
        customer.setLogin(dto.getLogin());

        customer.setCustomerId(dto.getCustomerId());
        customer.setCardId(dto.getCardId());
        customer.setFirstname(dto.getFirstname());
        customer.setLastname(dto.getLastname());
        customer.setBirthday(dto.getBirthday());
        customer.setIsActivated(dto.getIsActivated());

        switch (operation) {
            case CREATE:
                // for create (brand new address entity)
                customer.setAddress(dto.getAddress());

                HashSet<Authority> authorities = new HashSet<>();
                authorities.add(new Authority(AuthoritiesConstants.CUSTOMER));
                customer.setAuthorities(authorities);
                break;

            case UPDATE:
                // for edit (keep the address ID)
                Address add = customer.getAddress();
                Address dtoAdd = dto.getAddress();
                add.setStreetAndHousenumber(dtoAdd.getStreetAndHousenumber());
                add.setZip(dtoAdd.getZip());
                add.setCity(dtoAdd.getCity());
                add.setCountry(dtoAdd.getCountry());
                break;
        }
    }

    /**
     * This method returns the query to get information of customers for various lookup cases
     *
     */
    private CriteriaQuery<ViewCustomerDTO> getQuery(CriteriaBuilder builder, FindType findType,
                                                    String firstname, String lastname,
                                                    String login) {
        CriteriaQuery<ViewCustomerDTO> criteria = builder.createQuery(ViewCustomerDTO.class);
        Root<Customer> root = criteria.from(Customer.class);
        Join<Customer, Address> addressJoin = root.join("address", JoinType.LEFT);

        criteria.select(
                builder.construct(
                        ViewCustomerDTO.class,
                        root.get("userId"),
                        root.get("login"),
                        root.get("customerId"),
                        root.get("firstname"),
                        root.get("lastname"),
                        root.get("isActivated"),
                        root.get("birthday"),
                        root.get("cardId"),
                        addressJoin.get("streetAndHousenumber"),
                        addressJoin.get("zip"),
                        addressJoin.get("city"),
                        addressJoin.get("country")
                )
        );

        switch (findType) {
            case ALL:
                break;

            // Case insensitive search
            case BY_NAME:
                Path<String> firstPath = root.get("firstname");
                Expression<String> firstLower = builder.lower(firstPath);

                Path<String> lastPath = root.get("lastname");
                Expression<String> lastLower = builder.lower(lastPath);

                criteria.where(
                        builder.and(
                                builder.equal(firstLower, firstname.toLowerCase()),
                                builder.equal(lastLower, lastname.toLowerCase())
                        )
                );
                break;

            // Case insensitive search
            case BY_LOGIN:
                Path<String> loginPath = root.get("login");
                Expression<String> loginLower = builder.lower(loginPath);

                criteria.where(
                        builder.equal(loginLower, login.toLowerCase())
                );
                break;
        }

        return criteria;
    }
}
