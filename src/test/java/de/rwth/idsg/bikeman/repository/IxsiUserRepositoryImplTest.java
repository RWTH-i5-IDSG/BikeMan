package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.Application;
import de.rwth.idsg.bikeman.ixsi.repository.IxsiUserRepository;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
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

import static org.junit.Assert.assertTrue;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 30.10.2014
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("dev")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class IxsiUserRepositoryImplTest {

    @Inject IxsiUserRepository ixsiUserRepository;

    private static final String MAJOR_CUSTOMER_LOGIN = "major@bikeman.de";
    private static final String MAJOR_CUSTOMER_PASS = "major";

    @After
    public void methodeAfter() {
        log.info("---------");
    }

    @Test(expected = DatabaseException.class)
    public void test1_notExistingUser() throws DatabaseException {
        ixsiUserRepository.setUserToken(RandomStringUtils.randomAlphabetic(8), RandomStringUtils.randomAlphabetic(8));
    }

    @Test(expected = DatabaseException.class)
    public void test2_incorrectUserPass() throws DatabaseException {
        ixsiUserRepository.setUserToken(MAJOR_CUSTOMER_LOGIN, RandomStringUtils.randomAlphabetic(8));
    }

    @Test
    public void test4_updateAndValidateUserToken() throws DatabaseException {
        String userToken = ixsiUserRepository.setUserToken(MAJOR_CUSTOMER_LOGIN, MAJOR_CUSTOMER_PASS);
        log.debug("User token: {}", userToken);

        boolean isValid = ixsiUserRepository.validateUserToken(MAJOR_CUSTOMER_LOGIN, userToken);
        assertTrue(isValid);
    }
}