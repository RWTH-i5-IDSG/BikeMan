package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.*;
import de.rwth.idsg.bikeman.domain.Address_;
import de.rwth.idsg.bikeman.domain.BookedTariff_;
import de.rwth.idsg.bikeman.domain.CardAccount_;
import de.rwth.idsg.bikeman.domain.Customer_;
import de.rwth.idsg.bikeman.domain.Tariff_;
import de.rwth.idsg.bikeman.domain.login.Authority;
import de.rwth.idsg.bikeman.security.AuthoritiesConstants;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditAddressDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditCustomerDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewCustomerDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;

/**
 * Created by sgokay on 05.06.14.
 */
@Repository
@Slf4j
public class CustomerRepositoryImpl implements CustomerRepository {

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private TariffRepository tariffRepository;

    private enum Operation {CREATE, UPDATE}

    ;

    private enum FindType {ALL, BY_NAME, BY_LOGIN}

    ;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<ViewCustomerDTO> findAll() throws DatabaseException {
        try {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            return em.createQuery(
                    getQuery(builder, FindType.ALL, null, null)
            ).getResultList();

        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewCustomerDTO> findbyName(String name) throws DatabaseException {
        List<ViewCustomerDTO> list;
        try {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            list = em.createQuery(
                    getQuery(builder, FindType.BY_NAME, name, null)
            ).getResultList();

        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }

        if (list.isEmpty()) {
            throw new DatabaseException("No customer found with name " + name);
        } else {
            return list;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ViewCustomerDTO findbyLogin(String login) throws DatabaseException {
        try {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            return em.createQuery(
                    getQuery(builder, FindType.BY_LOGIN, null, login)
            ).getSingleResult();

        } catch (NoResultException e) {
            throw new DatabaseException("No customer found with login " + login, e);

        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CardAccount findByCardIdAndCardPin(String cardId, String cardPin) throws DatabaseException {

        final String query = "SELECT c FROM CardAccount c WHERE c.cardId = :cardId AND c.cardPin = :cardPin";

        try {
            return (CardAccount) em.createQuery(query)
                    .setParameter("cardId", cardId)
                    .setParameter("cardPin", cardPin)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new DatabaseException("No customer found with cardId " + cardId + " and cardPin " + cardPin, e);

        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activate(long userId) throws DatabaseException {
        Customer customer = getCustomerEntity(userId);
        try {
            customer.setIsActivated(true);
            em.merge(customer);
            log.debug("Activated customer {}", customer);

        } catch (Exception e) {
            throw new DatabaseException("Failed to activate customer with userId " + userId, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deactivate(long userId) throws DatabaseException {
        Customer customer = getCustomerEntity(userId);
        try {
            customer.setIsActivated(false);
            em.merge(customer);
            log.debug("Deactivated customer {}", customer);

        } catch (Exception e) {
            throw new DatabaseException("Failed to deactivate customer with userId " + userId, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CreateEditCustomerDTO dto) throws DatabaseException {
        Customer customer = new Customer();
        setFields(customer, dto, Operation.CREATE);

        try {
            em.persist(customer);
            log.debug("Created new customer {}", customer);

        } catch (EntityExistsException e) {
            throw new DatabaseException("This customer exists already.", e);

        } catch (Exception e) {
            throw new DatabaseException("Failed to create a new customer.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CreateEditCustomerDTO dto) throws DatabaseException {
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
            throw new DatabaseException("Failed to update customer with userId " + userId, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(long userId) throws DatabaseException {
        Customer customer = getCustomerEntity(userId);
        try {
            em.remove(customer);
            log.debug("Deleted customer {}", customer);

        } catch (Exception e) {
            throw new DatabaseException("Failed to delete customer with userId " + userId, e);
        }
    }

    /**
     * Returns a customer, or throws exception when no customer exists.
     */
    @Transactional(readOnly = true)
    private Customer getCustomerEntity(long userId) throws DatabaseException {
        Customer customer = em.find(Customer.class, userId);
        if (customer == null) {
            throw new DatabaseException("No customer with userId " + userId);
        } else {
            return customer;
        }
    }

    /**
     * This method sets the fields of the customer to the values in DTO.
     */
    private void setFields(Customer customer, CreateEditCustomerDTO dto, Operation operation) {

        // TODO: Should the login be changeable? Who sets the field? Clarify!
        customer.setLogin(dto.getLogin());

        customer.setCustomerId(dto.getCustomerId());
        customer.setFirstname(dto.getFirstname());
        customer.setLastname(dto.getLastname());
        customer.setBirthday(dto.getBirthday());
        customer.setIsActivated(dto.getIsActivated());

        if (dto.getPassword() != null) {
            customer.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        switch (operation) {
            case CREATE:
                // for create (brand new address entity)
                Address newAdd = new Address();
                CreateEditAddressDTO newDtoAdd = dto.getAddress();
                newAdd.setStreetAndHousenumber(newDtoAdd.getStreetAndHousenumber());
                newAdd.setZip(newDtoAdd.getZip());
                newAdd.setCity(newDtoAdd.getCity());
                newAdd.setCountry(newDtoAdd.getCountry());
                customer.setAddress(newAdd);

                CardAccount newCardAccount = new CardAccount();
                newCardAccount.setOwnerType(CustomerType.CUSTOMER);
                newCardAccount.setCardId(dto.getCardId());
                newCardAccount.setCardPin(dto.getCardPin());
                newCardAccount.setUser(customer);
                newCardAccount.setOperationState(OperationState.OPERATIVE);
                customer.setCardAccount(newCardAccount);

                BookedTariff newBookedTariff = new BookedTariff();
                newBookedTariff.setBookedFrom(new LocalDateTime());
                newBookedTariff.setBookedUntil(new LocalDateTime().plusYears(1));
                newBookedTariff.setTariff(tariffRepository.findByName(dto.getTariff()));
                newBookedTariff.setUsedCardAccount(newCardAccount);
                newCardAccount.setCurrentTariff(newBookedTariff);

                HashSet<Authority> authorities = new HashSet<>();
                authorities.add(new Authority(AuthoritiesConstants.CUSTOMER));
                customer.setAuthorities(authorities);
                break;

            case UPDATE:
                // for edit (keep the address ID)
                Address add = customer.getAddress();
                CreateEditAddressDTO dtoAdd = dto.getAddress();
                add.setStreetAndHousenumber(dtoAdd.getStreetAndHousenumber());
                add.setZip(dtoAdd.getZip());
                add.setCity(dtoAdd.getCity());
                add.setCountry(dtoAdd.getCountry());

                CardAccount cardAccount = customer.getCardAccount();
                cardAccount.setOwnerType(CustomerType.CUSTOMER);
                cardAccount.setCardId(dto.getCardId());
                cardAccount.setCardPin(dto.getCardPin());

                BookedTariff updateBookedTariff = new BookedTariff();
                updateBookedTariff.setBookedFrom(new LocalDateTime());
                updateBookedTariff.setBookedUntil(new LocalDateTime().plusYears(1));
                updateBookedTariff.setTariff(tariffRepository.findByName(dto.getTariff()));
                cardAccount.setCurrentTariff(updateBookedTariff);


                break;
        }
    }

    /**
     * This method returns the query to get information of customers for various lookup cases
     */
    private CriteriaQuery<ViewCustomerDTO> getQuery(CriteriaBuilder builder, FindType findType,
                                                    String name,
                                                    String login) {
        CriteriaQuery<ViewCustomerDTO> criteria = builder.createQuery(ViewCustomerDTO.class);
        Root<Customer> customer = criteria.from(Customer.class);
        Join<Customer, Address> address = customer.join(Customer_.address, JoinType.LEFT);
        Join<Customer, CardAccount> cardAccount = customer.join(Customer_.cardAccount, JoinType.LEFT);
        Join<CardAccount, BookedTariff> bookedTariff = cardAccount.join(CardAccount_.currentTariff, JoinType.LEFT);

        criteria.select(
                builder.construct(
                        ViewCustomerDTO.class,
                        customer.get(Customer_.userId),
                        customer.get(Customer_.login),
                        customer.get(Customer_.customerId),
                        customer.get(Customer_.firstname),
                        customer.get(Customer_.lastname),
                        customer.get(Customer_.isActivated),
                        customer.get(Customer_.birthday),
                        cardAccount.get(CardAccount_.cardId),
                        cardAccount.get(CardAccount_.cardPin),
                        bookedTariff.get(BookedTariff_.tariff).get(Tariff_.name),
                        address.get(Address_.streetAndHousenumber),
                        address.get(Address_.zip),
                        address.get(Address_.city),
                        address.get(Address_.country)
                )
        );

        switch (findType) {
            case ALL:
                break;

            // Case insensitive search
            case BY_NAME:
                Path<String> firstPath = customer.get(Customer_.firstname);
                Expression<String> firstLower = builder.lower(firstPath);

                Path<String> lastPath = customer.get(Customer_.lastname);
                Expression<String> lastLower = builder.lower(lastPath);

                /*
                * Frontend can send the search parameter with '+' sign between first and last name (or any substring of them).
                * We replace this with '%' since Postgre uses it to match any string of zero or more characters.
                *
                * The method returns a reference to the old object, when '+' does not occur in the string.
                * Therefore, no if-statement is needed.
                */
                name = name.replace("+", "%");

                criteria.where(
                        builder.like(
                                // Concatenate the two columns and search within the resulting representation
                                // for flexibility, since the user can search by first or last name, or both.
                                builder.concat(firstLower, lastLower),

                                // Find a matching sequence anywhere within the concatenated representation
                                ("%" + name + "%").toLowerCase()
                        )
                );

                break;

            // Case insensitive search
            case BY_LOGIN:
                Path<String> loginPath = customer.get(Customer_.login);
                Expression<String> loginLower = builder.lower(loginPath);

                criteria.where(
                        builder.equal(loginLower, login.toLowerCase())
                );
                break;
        }

        return criteria;
    }
}