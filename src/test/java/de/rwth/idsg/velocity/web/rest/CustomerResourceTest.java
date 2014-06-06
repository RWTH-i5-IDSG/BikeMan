package de.rwth.idsg.velocity.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import de.rwth.idsg.velocity.Application;
import de.rwth.idsg.velocity.domain.Customer;
import de.rwth.idsg.velocity.repository.CustomerRepository;


/**
 * Test class for the CustomerResource REST controller.
 *
 * @see CustomerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class })
@ActiveProfiles("dev")
public class CustomerResourceTest {
    
    private static final Long DEFAULT_ID = new Long(1L);

    private static final LocalDate DEFAULT_SAMPLE_DATE_ATTR = new LocalDate(0L);

    private static final LocalDate UPD_SAMPLE_DATE_ATTR = new LocalDate();

    private static final String DEFAULT_SAMPLE_TEXT_ATTR = "sampleTextAttribute";

    private static final String UPD_SAMPLE_TEXT_ATTR = "sampleTextAttributeUpt";

    @Inject
    private CustomerRepository customerRepository;

    private MockMvc restCustomerMockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomerResource customerResource = new CustomerResource();
        ReflectionTestUtils.setField(customerResource, "customerRepository", customerRepository);

        this.restCustomerMockMvc = MockMvcBuilders.standaloneSetup(customerResource).build();
    }

    @Test
    public void test1_getAllCustomers() throws Exception {
        restCustomerMockMvc.perform(get("/app/rest/customers"))
                .andExpect(status().isOk());
    }

    @Test
    public void test2_getByName() throws Exception {
        restCustomerMockMvc.perform(get("/app/rest/customers/name/Wollo+Kluth"))
                .andExpect(status().isOk());
    }

    @Test
    public void test3_getByEmail() throws Exception {
        restCustomerMockMvc.perform(get("/app/rest/customers/email/hu@mail.com"))
                .andExpect(status().isOk());
    }

    @Test
    public void test4_getByLogin() throws Exception {
        restCustomerMockMvc.perform(get("/app/rest/customers/login/admin"))
                .andExpect(status().isOk());
    }

//    @Ignore
//    public void testCRUDCustomer() throws Exception {
//
//    	// Create Customer
//    	restCustomerMockMvc.perform(post("/app/rest/customers")
//    			.contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .content(TestUtil.convertObjectToJsonBytes(customer)))
//                .andExpect(status().isOk());
//
//    	// Read Customer
//    	restCustomerMockMvc.perform(get("/app/rest/customers/{id}", DEFAULT_ID))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
//    			.andExpect(jsonPath("$.sampleDateAttribute").value(DEFAULT_SAMPLE_DATE_ATTR.toString()))
//    			.andExpect(jsonPath("$.sampleTextAttribute").value(DEFAULT_SAMPLE_TEXT_ATTR));
//
//    	// Update Customer
////    	customer.setSampleDateAttribute(UPD_SAMPLE_DATE_ATTR);
////    	customer.setSampleTextAttribute(UPD_SAMPLE_TEXT_ATTR);
//
//    	restCustomerMockMvc.perform(post("/app/rest/customers")
//    			.contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .content(TestUtil.convertObjectToJsonBytes(customer)))
//                .andExpect(status().isOk());
//
//    	// Read updated Customer
//    	restCustomerMockMvc.perform(get("/app/rest/customers/{id}", DEFAULT_ID))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
//    			.andExpect(jsonPath("$.sampleDateAttribute").value(UPD_SAMPLE_DATE_ATTR.toString()))
//    			.andExpect(jsonPath("$.sampleTextAttribute").value(UPD_SAMPLE_TEXT_ATTR));
//
//    	// Delete Customer
//    	restCustomerMockMvc.perform(delete("/app/rest/customers/{id}", DEFAULT_ID)
//                .accept(TestUtil.APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk());
//
//    	// Read nonexisting Customer
//    	restCustomerMockMvc.perform(get("/app/rest/customers/{id}", DEFAULT_ID)
//                .accept(TestUtil.APPLICATION_JSON_UTF8))
//                .andExpect(status().isNotFound());
//
//    }
}
