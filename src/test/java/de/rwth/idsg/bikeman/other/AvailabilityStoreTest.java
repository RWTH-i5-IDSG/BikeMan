package de.rwth.idsg.bikeman.other;

import de.rwth.idsg.bikeman.Application;
import de.rwth.idsg.bikeman.ixsi.impl.AvailabilityStore;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
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
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertThat;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 04.11.2014
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("dev")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class AvailabilityStoreTest {

    @Inject private AvailabilityStore store;

    private static final String systemId_1 = "System-1";
    private static final String systemId_2 = "System-2";

    @After
    public void clearStore() {
        store.clear();
    }

    @Test
    public void test1_System1() {
        store.subscribe(systemId_1, Arrays.asList("11", "12", "13", "14"));
        List<String> list_1 = store.getSubscriptions(systemId_1);

        logWithMethodName();
        assertThat(list_1, CoreMatchers.hasItems("11", "12", "13", "14"));
    }

    @Test
    public void test2_System1_OneMore() {
        store.subscribe(systemId_1, Arrays.asList("11", "12", "13", "14"));

        store.subscribe(systemId_1, Arrays.asList("5765"));
        List<String> list_1 = store.getSubscriptions(systemId_1);

        logWithMethodName();
        assertThat(list_1, CoreMatchers.hasItems("11", "12", "13", "14", "5765"));
    }

    @Test
    public void test3_System2() {
        store.subscribe(systemId_1, Arrays.asList("11", "12", "13", "14"));
        store.subscribe(systemId_2, Arrays.asList("21", "22", "23", "24"));
        List<String> list_2 = store.getSubscriptions(systemId_2);

        logWithMethodName();
        assertThat(list_2, CoreMatchers.hasItems("21", "22", "23", "24"));
    }

    @Test
    public void test4_System1_Duplicate() {
        store.subscribe(systemId_1, Arrays.asList("11", "12", "13", "14"));
        store.subscribe(systemId_2, Arrays.asList("21", "22", "23", "24"));

        store.subscribe(systemId_1, Arrays.asList("13", "14"));
        List<String> list_1 = store.getSubscriptions(systemId_1);

        logWithMethodName();
        assertThat(list_1, CoreMatchers.hasItems("11", "12", "13", "14"));
    }

    @Test
    public void test5_System1and2_Mix() {
        store.subscribe(systemId_1, Arrays.asList("11", "12", "13", "14"));
        store.subscribe(systemId_2, Arrays.asList("21", "22", "23", "24"));

        // system1 subscribes to target of system2
        store.subscribe(systemId_1, Arrays.asList("22"));
        List<String> list_1 = store.getSubscriptions(systemId_1);

        logWithMethodName();
        assertThat(list_1, CoreMatchers.hasItems("11", "12", "13", "14", "22"));
    }

    @Test
    public void test6_GetSystems() {
        store.subscribe(systemId_1, Arrays.asList("11", "12", "13", "14"));
        store.subscribe(systemId_2, Arrays.asList("21", "22", "23", "24"));

        store.subscribe(systemId_1, Arrays.asList("22"));

        Set<String> systems = store.getSubscribedSystems("22");
        assertThat(systems, CoreMatchers.hasItems(systemId_1, systemId_2));
    }

    @Test
    public void test7_System1_Unsubscribe() {
        store.subscribe(systemId_1, Arrays.asList("11", "12", "13", "14"));
        store.subscribe(systemId_2, Arrays.asList("21", "22", "23", "24"));

        store.unsubscribe(systemId_1, Arrays.asList("11"));
        List<String> temp = store.getSubscriptions(systemId_1);

        logWithMethodName();
        assertThat(temp, CoreMatchers.hasItems("12", "13", "14"));
    }

    @Test
    public void test8_RandomSystemForAll() {
        store.subscribe(systemId_1, Arrays.asList("11", "12", "13", "14"));
        store.subscribe(systemId_2, Arrays.asList("21", "22", "23", "24"));

        store.subscribe("random", Arrays.asList("11", "12", "13", "14", "21", "22", "23", "24"));
        List<String> temp = store.getSubscriptions("random");

        logWithMethodName();
        assertThat(temp, CoreMatchers.hasItems("11", "12", "13", "14", "21", "22", "23", "24"));
    }

    private void logWithMethodName() {
        StackTraceElement stackTraceElements[] = (new Throwable()).getStackTrace();
        String methodStr = stackTraceElements[1].getMethodName();
        log.info("{} : {}", methodStr, store.toString());
    }
}
