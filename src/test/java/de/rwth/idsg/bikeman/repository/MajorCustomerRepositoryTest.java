package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.Application;
import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.domain.CustomerType;
import de.rwth.idsg.bikeman.domain.MajorCustomer;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.domain.login.User;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditMajorCustomerDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewMajorCustomerDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by swam on 16/10/14.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("dev")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

@Slf4j
public class MajorCustomerRepositoryTest {

    @Inject
    private MajorCustomerRepository majorCustomerRepository;

    @Inject
    private CardAccountRepository cardAccountRepository;

    @Inject
    private UserRepository userRepository;

    @Test
    public void test1_createMajorCustomers() {

        for (int i = 0; i < 3; i++) {

            CreateEditMajorCustomerDTO newMajorCustomer = new CreateEditMajorCustomerDTO();
            newMajorCustomer.setLogin(RandomStringUtils.randomAlphabetic(6) + "@testmail.de");
            newMajorCustomer.setPassword(RandomStringUtils.randomAlphabetic(12));
            newMajorCustomer.setName(RandomStringUtils.randomAlphabetic(8));

            try {
                majorCustomerRepository.create(newMajorCustomer);
                Assert.assertNull(newMajorCustomer.getUserId());
                log.debug("Successfully created majorCustomer: ", newMajorCustomer);
            } catch (DatabaseException ex) {
                log.error("Creating a new majorcustomer failed: ", ex);
            }

        }
    }


    @Test
    public void test2_findAll() {

        try {
            List<ViewMajorCustomerDTO> viewMajorCustomerDTOList = majorCustomerRepository.findAll();
            log.debug("Successfully found all majorCustomers: ", viewMajorCustomerDTOList);
        } catch (DatabaseException ex) {
            log.error("Creating a new majorcustomer failed: ", ex);
        }

    }

    @Test
    public void test3_findOne() {

        Set<CardAccount> cardAccounts = new HashSet<>();

        try {
            List<ViewMajorCustomerDTO> viewMajorCustomerDTOList = majorCustomerRepository.findAll();

            User user = userRepository.findOne(viewMajorCustomerDTOList.get(0).getUserId());

            for (int i = 0; i < 5; i++) {
                CardAccount cardAccount = new CardAccount();
                cardAccount.setInTransaction(false);
                cardAccount.setOwnerType(CustomerType.MAJOR_CUSTOMER);
                cardAccount.setCardId(RandomStringUtils.randomNumeric(10));
                cardAccount.setCardPin(RandomStringUtils.randomNumeric(4));
                cardAccount.setOperationState(OperationState.OPERATIVE);
                cardAccount.setUser(user);

                cardAccounts.add(cardAccount);
            }

            ((MajorCustomer)user).setCardAccounts(cardAccounts);

            userRepository.save(user);

            ViewMajorCustomerDTO viewMajorCustomerDTO = majorCustomerRepository.findOne(user.getUserId());
            log.debug("Successfully found one majorCustomer: ", viewMajorCustomerDTO);
        } catch (DatabaseException ex) {
            log.error("Creating a new majorcustomer failed: ", ex);
        }

    }

}
