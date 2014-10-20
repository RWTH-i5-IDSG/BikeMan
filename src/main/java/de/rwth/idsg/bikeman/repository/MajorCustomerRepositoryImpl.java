package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.domain.CardAccount_;
import de.rwth.idsg.bikeman.domain.MajorCustomer;
import de.rwth.idsg.bikeman.domain.MajorCustomer_;
import de.rwth.idsg.bikeman.domain.login.Authority;
import de.rwth.idsg.bikeman.domain.login.User_;
import de.rwth.idsg.bikeman.security.AuthoritiesConstants;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditMajorCustomerDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewCardAccountDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewCustomerDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewMajorCustomerDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by swam on 16/10/14.
 */

@Repository
@Slf4j
public class MajorCustomerRepositoryImpl implements MajorCustomerRepository {

    private enum Operation { CREATE, UPDATE };
    private enum FindType { ALL, BY_ID, BY_LOGIN };

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<ViewMajorCustomerDTO> findAll() throws DatabaseException {
        try {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            return em.createQuery(
                    getMajorCustomerQuery(builder, FindType.ALL, null, null)
            ).getResultList();

        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ViewMajorCustomerDTO findByLogin(String login) throws DatabaseException {
        try {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            return em.createQuery(
                    getMajorCustomerQuery(builder, FindType.BY_LOGIN, login, null)
            ).getSingleResult();

        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ViewMajorCustomerDTO findOne(long majorCustomerId) throws DatabaseException {

        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<ViewMajorCustomerDTO> criteria = this.getMajorCustomerQuery(builder, FindType.BY_ID, null, majorCustomerId);
        ViewMajorCustomerDTO majorCustomerDTO;

        try {
            majorCustomerDTO = em.createQuery(criteria).getSingleResult();
        } catch (Exception e) {
            throw new DatabaseException("Failed to find majorcustomer with majorCustomerId: " + majorCustomerId, e);
        }

        CriteriaQuery<ViewCardAccountDTO> cardAccountCriteria = builder.createQuery(ViewCardAccountDTO.class);
        Root<CardAccount> cardAccount = cardAccountCriteria.from(CardAccount.class);

        cardAccountCriteria.select(
                builder.construct(
                        ViewCardAccountDTO.class,
                        cardAccount.get(CardAccount_.cardId),
                        cardAccount.get(CardAccount_.cardPin),
                        cardAccount.get(CardAccount_.inTransaction),
                        cardAccount.get(CardAccount_.operationState)
                )
        ).where(builder.equal(cardAccount.get(CardAccount_.user).get(User_.userId), majorCustomerId));

        List<ViewCardAccountDTO> list;

        try {
            list = em.createQuery(cardAccountCriteria).getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Failed to get cards for major customer", e);
        }

        if (list == null) {
            majorCustomerDTO.setCardAccountDTOs(new HashSet<ViewCardAccountDTO>());
            return majorCustomerDTO;
        }

        Set<ViewCardAccountDTO> set = new HashSet<>(list);
        majorCustomerDTO.setCardAccountDTOs(set);

        return majorCustomerDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CreateEditMajorCustomerDTO dto) throws DatabaseException {
        MajorCustomer majorCustomer = new MajorCustomer();
        setFields(majorCustomer, dto, Operation.CREATE);

        try {
            em.persist(majorCustomer);
            log.debug("Created new majorcustomer {}", majorCustomer);

        } catch (EntityExistsException e) {
            throw new DatabaseException("This customer exists already.", e);

        } catch (Exception e) {
            throw new DatabaseException("Failed to create a new customer.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CreateEditMajorCustomerDTO dto) throws DatabaseException {
        final Long userId = dto.getUserId();
        if (userId == null) {
            return;
        }

        MajorCustomer majorCustomer = getMajorCustomerEntity(userId);
        try {
            setFields(majorCustomer, dto, Operation.UPDATE);
            em.merge(majorCustomer);
            log.debug("Updated majorcustomer {}", majorCustomer);

        } catch (Exception e) {
            throw new DatabaseException("Failed to update majorcustomer with userId " + userId, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(long userId) throws DatabaseException {
        MajorCustomer majorCustomer = getMajorCustomerEntity(userId);
        try {
            em.remove(majorCustomer);
            log.debug("Deleted majorcustomer {}", majorCustomer);

        } catch (Exception e) {
            throw new DatabaseException("Failed to delete majorcustomer with userId " + userId, e);
        }
    }

    /**
     * Returns a customer, or throws exception when no customer exists.
     *
     */
    @Transactional(readOnly = true)
    private MajorCustomer getMajorCustomerEntity(long userId) throws DatabaseException {
        MajorCustomer majorCustomer = em.find(MajorCustomer.class, userId);
        if (majorCustomer == null) {
            throw new DatabaseException("No majorcustomer with userId " + userId);
        } else {
            return majorCustomer;
        }
    }

    /**
     * This method sets the fields of the customer to the values in DTO.
     *
     */
    private void setFields(MajorCustomer majorCustomer, CreateEditMajorCustomerDTO dto, Operation operation) {

        // TODO: Should the login be changeable? Who sets the field? Clarify!
        majorCustomer.setLogin(dto.getLogin());
        majorCustomer.setName(dto.getName());
        majorCustomer.setPassword(dto.getPassword());

        switch (operation) {
            case CREATE:

                HashSet<Authority> authorities = new HashSet<>();
                authorities.add(new Authority(AuthoritiesConstants.MAJOR_CUSTOMER));
                majorCustomer.setAuthorities(authorities);
                break;

            case UPDATE:

                break;
        }
    }

    /**
     * This method returns the query to get information of customers for various lookup cases
     *
     */
    private CriteriaQuery<ViewMajorCustomerDTO> getMajorCustomerQuery(CriteriaBuilder builder, FindType findType, String login, Long majorCustomerId) {
        CriteriaQuery<ViewMajorCustomerDTO> criteria = builder.createQuery(ViewMajorCustomerDTO.class);
        Root<MajorCustomer> majorCustomer = criteria.from(MajorCustomer.class);

        criteria.select(
                builder.construct(
                        ViewMajorCustomerDTO.class,
                        majorCustomer.get(MajorCustomer_.userId),
                        majorCustomer.get(MajorCustomer_.login),
                        majorCustomer.get(MajorCustomer_.name)
                )
        );

        switch (findType) {
            case ALL:
                break;

            case BY_LOGIN:
                criteria.where(builder.equal(majorCustomer.get(MajorCustomer_.login), login));
                break;

            case BY_ID:
                criteria.where(builder.equal(majorCustomer.get(MajorCustomer_.userId), majorCustomerId));
                break;
        }

        return criteria;
    }

    private CriteriaQuery<ViewMajorCustomerDTO> getQueryWithCardAccounts(CriteriaBuilder builder) {
        CriteriaQuery<ViewMajorCustomerDTO> criteria = builder.createQuery(ViewMajorCustomerDTO.class);
        Root<MajorCustomer> majorCustomer = criteria.from(MajorCustomer.class);

        criteria.select(
                builder.construct(
                        ViewMajorCustomerDTO.class,
                        majorCustomer.get(MajorCustomer_.userId),
                        majorCustomer.get(MajorCustomer_.login),
                        majorCustomer.get(MajorCustomer_.name)
                )
        );

        return criteria;
    }
}
