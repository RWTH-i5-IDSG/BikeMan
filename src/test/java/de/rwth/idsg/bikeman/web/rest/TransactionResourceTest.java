package de.rwth.idsg.bikeman.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
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

import de.rwth.idsg.bikeman.Application;
import de.rwth.idsg.bikeman.repository.TransactionRepository;


/**
 * Test class for the TransactionResource REST controller.
 *
 * @see TransactionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class })
@ActiveProfiles("dev")
public class TransactionResourceTest {

    private static final Long DEFAULT_ID = new Long(1L);

    private static final LocalDate DEFAULT_SAMPLE_DATE_ATTR = new LocalDate(0L);

    private static final LocalDate UPD_SAMPLE_DATE_ATTR = new LocalDate();

    private static final String DEFAULT_SAMPLE_TEXT_ATTR = "sampleTextAttribute";

    private static final String UPD_SAMPLE_TEXT_ATTR = "sampleTextAttributeUpt";

    @Inject
    private TransactionRepository transactionRepository;

    private MockMvc restTransactionMockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TransactionResource transactionResource = new TransactionResource();
        ReflectionTestUtils.setField(transactionResource, "transactionRepository", transactionRepository);

        this.restTransactionMockMvc = MockMvcBuilders.standaloneSetup(transactionResource).build();
    }

    @Test
    public void getAllTransactions() throws Exception {
        restTransactionMockMvc.perform(get("/app/rest/transactions"))
                .andExpect(status().isOk());
    }

    @Test
    public void getOpenTransactions() throws Exception {
        restTransactionMockMvc.perform(get("/app/rest/transactions/open"))
                .andExpect(status().isOk());
    }

    @Test
    public void getClosedTransactions() throws Exception {
        restTransactionMockMvc.perform(get("/app/rest/transactions/closed"))
                .andExpect(status().isOk());
    }

//    @Test
//    public void testCRUDTransaction() throws Exception {
//
//    	// Create Transaction
//    	restTransactionMockMvc.perform(post("/app/rest/transactions")
//    			.contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .content(TestUtil.convertObjectToJsonBytes(transaction)))
//                .andExpect(status().isOk());
//
//    	// Read Transaction
//    	restTransactionMockMvc.perform(get("/app/rest/transactions/{id}", DEFAULT_ID))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
//    			.andExpect(jsonPath("$.sampleDateAttribute").value(DEFAULT_SAMPLE_DATE_ATTR.toString()))
//    			.andExpect(jsonPath("$.sampleTextAttribute").value(DEFAULT_SAMPLE_TEXT_ATTR));
//
//    	// Update Transaction
////    	transaction.setSampleDateAttribute(UPD_SAMPLE_DATE_ATTR);
////    	transaction.setSampleTextAttribute(UPD_SAMPLE_TEXT_ATTR);
//
//    	restTransactionMockMvc.perform(post("/app/rest/transactions")
//    			.contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .content(TestUtil.convertObjectToJsonBytes(transaction)))
//                .andExpect(status().isOk());
//
//    	// Read updated Transaction
//    	restTransactionMockMvc.perform(get("/app/rest/transactions/{id}", DEFAULT_ID))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
//    			.andExpect(jsonPath("$.sampleDateAttribute").value(UPD_SAMPLE_DATE_ATTR.toString()))
//    			.andExpect(jsonPath("$.sampleTextAttribute").value(UPD_SAMPLE_TEXT_ATTR));
//
//    	// Delete Transaction
//    	restTransactionMockMvc.perform(delete("/app/rest/transactions/{id}", DEFAULT_ID)
//                .accept(TestUtil.APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk());
//
//    	// Read nonexisting Transaction
//    	restTransactionMockMvc.perform(get("/app/rest/transactions/{id}", DEFAULT_ID)
//                .accept(TestUtil.APPLICATION_JSON_UTF8))
//                .andExpect(status().isNotFound());
//
//    }
}
