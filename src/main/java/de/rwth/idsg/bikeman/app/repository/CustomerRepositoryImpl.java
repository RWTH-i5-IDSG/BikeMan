package de.rwth.idsg.bikeman.app.repository;

import de.rwth.idsg.bikeman.app.dto.ViewCustomerDTO;
import de.rwth.idsg.bikeman.app.exception.AppErrorCode;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;

@Repository("CustomerRepositoryImplApp")
public class CustomerRepositoryImpl implements CustomerRepository {

    @PersistenceContext
    private EntityManager em;

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

}
