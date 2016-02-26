package de.rwth.idsg.bikeman.app.repository;

import de.rwth.idsg.bikeman.app.dto.CreateAddressDTO;
import de.rwth.idsg.bikeman.app.dto.CreateCustomerDTO;
import de.rwth.idsg.bikeman.app.dto.ViewCustomerDTO;
import de.rwth.idsg.bikeman.app.exception.AppErrorCode;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.domain.*;
import de.rwth.idsg.bikeman.domain.Address_;
import de.rwth.idsg.bikeman.domain.Customer_;
import de.rwth.idsg.bikeman.security.AuthoritiesConstants;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;

@Repository
@Slf4j
public class AppCustomerRepositoryImpl implements AppCustomerRepository {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private de.rwth.idsg.bikeman.repository.TariffRepository tariffRepository;


    public ViewCustomerDTO findOne(Long userId) throws AppException {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<ViewCustomerDTO> criteria = builder.createQuery(ViewCustomerDTO.class);

        Root<Customer> customer = criteria.from(Customer.class);
        Join<Customer, Address> address = customer.join(Customer_.address, JoinType.LEFT);

        criteria.select(
                builder.construct(
                        ViewCustomerDTO.class,
                        customer.get(Customer_.customerId),
                        customer.get(Customer_.login),
                        customer.get(Customer_.firstname),
                        customer.get(Customer_.lastname),
                        customer.get(Customer_.isActivated),
                        customer.get(Customer_.birthday),

                        address.get(Address_.streetAndHousenumber),
                        address.get(Address_.zip),
                        address.get(Address_.city),
                        address.get(Address_.country)
                )
        ).where(
                builder.equal(customer.get(Customer_.userId), userId)
        );

        try {
            return em.createQuery(criteria).getSingleResult();
        } catch (NoResultException e) {
            throw new AppException("Failed to find customer with userId " + userId, e, AppErrorCode.CONSTRAINT_FAILED);
        } catch (Exception e) {
            throw new AppException("Failed during database operation", e, AppErrorCode.DATABASE_OPERATION_FAILED);
        }
    }

    @Override
    @Transactional
    public Optional<Customer> findByLogin (String login) {
        final String q = "SELECT c FROM Customer c WHERE UPPER(c.login) = UPPER(:login)";

        try {
            return Optional.of(em.createQuery(q, Customer.class)
                     .setParameter("login", login)
                     .getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = false)
    public CreateCustomerDTO create(CreateCustomerDTO dto) {
        final String openTransactionsQuery = "SELECT COUNT(c) FROM Customer c " +
            "WHERE c.login = :login";

        Long customerExists = em.createQuery(openTransactionsQuery, Long.class)
            .setParameter("login", dto.getLogin())
            .getSingleResult();

        if (customerExists > 0) {
            throw new AppException("Login name already exists.", AppErrorCode.CONSTRAINT_FAILED);
        }

        Customer customer = new Customer();

        customer.setLogin(dto.getLogin());
        // TODO: change to a "real" customerId
        customer.setCustomerId("A" + LocalDateTime.now().getYear() + LocalDateTime.now().getMillisOfDay());
        customer.setFirstname(dto.getFirstname());
        customer.setLastname(dto.getLastname());
        customer.setBirthday(dto.getBirthday());
        customer.setIsActivated(false);

        Address newAdd = new Address();
        CreateAddressDTO newDtoAdd = dto.getAddress();
        newAdd.setStreetAndHousenumber(newDtoAdd.getStreetAndHousenumber());
        newAdd.setZip(newDtoAdd.getZip());
        newAdd.setCity(newDtoAdd.getCity());
        newAdd.setCountry(newDtoAdd.getCountry());
        customer.setAddress(newAdd);

        CardAccount newCardAccount = new CardAccount();
        newCardAccount.setOwnerType(CustomerType.CUSTOMER);
        // TODO: let database assign a CardId
        newCardAccount.setCardId("0A" + Integer.toHexString(new Random().nextInt(Integer.MAX_VALUE)));
        newCardAccount.setCardPin(dto.getCardPin());
        newCardAccount.setUser(customer);
        newCardAccount.setOperationState(OperationState.OPERATIVE);
        customer.setCardAccount(newCardAccount);

        BookedTariff newBookedTariff = new BookedTariff();
        newBookedTariff.setBookedFrom(new LocalDateTime());

        newBookedTariff.setBookedUntil(null);
        newBookedTariff.setTariff(tariffRepository.findByName(TariffType.Ticket2000));
        newBookedTariff.setUsedCardAccount(newCardAccount);
        newCardAccount.setCurrentTariff(newBookedTariff);
        newCardAccount.setAutoRenewTariff(true);

        HashSet<Authority> authorities = new HashSet<>();
        authorities.add(new Authority(AuthoritiesConstants.CUSTOMER));
        customer.setAuthorities(authorities);

        try {
            em.persist(customer);
            log.debug("Created new customer {}", customer);

        } catch (EntityExistsException e) {
            throw new AppException("This customer exists already.", e, AppErrorCode.CONSTRAINT_FAILED);

        } catch (Exception e) {
            throw new AppException("Failed to create a new customer.", e, AppErrorCode.DATABASE_OPERATION_FAILED);
        }

        dto.getBankAccount().setIBAN(dto.getBankAccount().getIBAN().substring(0, 5) + "*************");

        return dto;
    }

}
