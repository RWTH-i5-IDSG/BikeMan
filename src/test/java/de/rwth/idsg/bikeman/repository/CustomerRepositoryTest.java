package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.Application;
import de.rwth.idsg.bikeman.domain.Customer;
import de.rwth.idsg.bikeman.domain.login.User;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditAddressDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditCustomerDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
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
import java.util.Date;
import java.util.Random;
import java.util.UUID;


/**
 * Created by swam on 15/10/14.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("dev")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

@Slf4j
public class CustomerRepositoryTest {

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private UserRepository userRepository;

    @Test
    public void test1_createCustomer() {

        CreateEditAddressDTO newAddress = new CreateEditAddressDTO();
        newAddress.setCity("TestCity");
        newAddress.setCountry("TestCountry");
        newAddress.setStreetAndHousenumber("TestStreet 23");

        CreateEditCustomerDTO newCustomer = new CreateEditCustomerDTO();
        newCustomer.setAddress(newAddress);
        newCustomer.setBirthday(LocalDate.now().minusYears(20).minusWeeks(10));
        newCustomer.setFirstname("TestFirstName");
        newCustomer.setLastname("TestLastName");
        newCustomer.setCustomerId(UUID.randomUUID().toString());
        newCustomer.setIsActivated(true);
        newCustomer.setLogin(new Date().getTime() + "@test.com");
        String generatedCardId = UUID.randomUUID().toString();
        newCustomer.setCardId(generatedCardId);
        newCustomer.setCardPin(new Random().nextInt(9000));

        try {
            customerRepository.create(newCustomer);

            log.info("Find Customer with CustomerId: {}, CardId and CardPin.", newCustomer.getCustomerId());

            long userId = customerRepository.findByCardIdAndCardPin(generatedCardId, newCustomer.getCardPin());

            log.info("Found userId: {}", userId);

            User user = userRepository.findOne(userId);

            Assert.assertEquals(newCustomer.getLogin(), user.getLogin());
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

}
