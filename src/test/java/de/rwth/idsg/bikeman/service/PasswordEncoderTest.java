package de.rwth.idsg.bikeman.service;

import de.rwth.idsg.bikeman.ixsi.schema.SystemIDType;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * Created by swam on 29/10/14.
 */

@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PasswordEncoderTest {

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    private long systemMillis;

    @Before
    public void methodeBefore() {
//        log.info(new DateTime().toString());
        systemMillis = System.currentTimeMillis();
    }

    @After
    public void methodeAfter() {
//        log.info(new DateTime().toString());
        log.info("Duration: {}", System.currentTimeMillis() - systemMillis);
        log.info("---------");
    }

    @Test
    public void test1_createEncodedPasswords() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);

        log.info("Encoded 'admin': {}", passwordEncoder.encode("admin"));
    }
    @Test
    public void test2_createEncodedPasswords() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(9);
        log.info("Encoded 'user': {}", passwordEncoder.encode("user"));
    }
    @Test
    public void test3_createEncodedPasswords() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        log.info("Encoded 'manager': {}", passwordEncoder.encode("manager"));
    }
    @Test
    public void test4_createEncodedPasswords() {
        StandardPasswordEncoder passwordEncoder = new StandardPasswordEncoder();
        log.info("StandardEncoded 'manager': {}", passwordEncoder.encode("manager"));
    }
}
