package de.rwth.idsg.bikeman.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
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

    @After
    public void methodeAfter() {
        log.info("---------");
    }

    @Test
    public void test1_createEncodedPasswords() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);

        long start = System.currentTimeMillis();
        String str = passwordEncoder.encode("admin");
        log.info("Duration: {}", System.currentTimeMillis() - start);

        log.info("Encoded 'admin': {}", str);
    }
    @Test
    public void test2_createEncodedPasswords() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(9);

        long start = System.currentTimeMillis();
        String str = passwordEncoder.encode("user");
        log.info("Duration: {}", System.currentTimeMillis() - start);

        log.info("Encoded 'user': {}", str);
    }
    @Test
    public void test3_createEncodedPasswords() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

        long start = System.currentTimeMillis();
        String str = passwordEncoder.encode("manager");
        log.info("Duration: {}", System.currentTimeMillis() - start);

        log.info("Encoded 'manager': {}", str);
    }
    @Test
    public void test4_createEncodedPasswords() {
        StandardPasswordEncoder passwordEncoder = new StandardPasswordEncoder();

        long start = System.currentTimeMillis();
        String str = passwordEncoder.encode("manager");
        log.info("Duration: {}", System.currentTimeMillis() - start);

        log.info("StandardEncoded 'manager': {}", str);
    }
}
