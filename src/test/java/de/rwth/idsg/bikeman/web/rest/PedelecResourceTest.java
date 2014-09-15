package de.rwth.idsg.bikeman.web.rest;

import de.rwth.idsg.bikeman.Application;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.repository.PedelecRepository;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditPedelecDTO;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Ignore;
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

import javax.inject.Inject;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Test class for the PedelecResource REST controller.
 *
 * @see PedelecResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class })
@ActiveProfiles("dev")
public class PedelecResourceTest {
    
    private static final Long DEFAULT_ID = new Long(1L);

    private static final LocalDate DEFAULT_SAMPLE_DATE_ATTR = new LocalDate(0L);

    private static final LocalDate UPD_SAMPLE_DATE_ATTR = new LocalDate();

    private static final String DEFAULT_SAMPLE_TEXT_ATTR = "sampleTextAttribute";

    private static final String UPD_SAMPLE_TEXT_ATTR = "sampleTextAttributeUpt";

    private int REPEAT_COUNT = 500;

    @Inject
    private PedelecRepository pedelecRepository;

    private MockMvc restPedelecMockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PedelecResource pedelecResource = new PedelecResource();
        ReflectionTestUtils.setField(pedelecResource, "pedelecRepository", pedelecRepository);

        restPedelecMockMvc = MockMvcBuilders.standaloneSetup(pedelecResource).build();
    }

    @Ignore
    public void pedelecDtoMissingFields() throws Exception {
        CreateEditPedelecDTO ped = new CreateEditPedelecDTO();

        restPedelecMockMvc.perform(post("/app/rest/pedelecs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ped)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void pedelecDtoWithFields() throws Exception {

        List<OperationState> states = new ArrayList<>();
        Collections.addAll(states, OperationState.values());

        for (int n=1; n <= REPEAT_COUNT; n++ ) {
            Collections.shuffle(states);

            CreateEditPedelecDTO ped = new CreateEditPedelecDTO();
            ped.setManufacturerId(UUID.randomUUID().toString());
            ped.setState(states.get(0));

            restPedelecMockMvc.perform(post("/app/rest/pedelecs")
                    .contentType(TestUtil.APPLICATION_JSON_UTF8)
                    .content(TestUtil.convertObjectToJsonBytes(ped)))
                    .andExpect(status().isOk());
        }
    }

    @Ignore
    public void getAllPedelecs() throws Exception {

    	restPedelecMockMvc.perform(get("/app/rest/pedelecs"))
                .andExpect(status().isOk());
    }

//    public void testCRUDPedelec() throws Exception {
//
//    	// Create Pedelec
//    	restPedelecMockMvc.perform(post("/app/rest/pedelecs")
//    			.contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .content(TestUtil.convertObjectToJsonBytes(pedelec)))
//                .andExpect(status().isOk());
//
//    	// Read Pedelec
//    	restPedelecMockMvc.perform(get("/app/rest/pedelecs/{id}", DEFAULT_ID))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
//    			.andExpect(jsonPath("$.sampleDateAttribute").value(DEFAULT_SAMPLE_DATE_ATTR.toString()))
//    			.andExpect(jsonPath("$.sampleTextAttribute").value(DEFAULT_SAMPLE_TEXT_ATTR));
//
//    	// Update Pedelec
////    	pedelec.setSampleDateAttribute(UPD_SAMPLE_DATE_ATTR);
////    	pedelec.setSampleTextAttribute(UPD_SAMPLE_TEXT_ATTR);
//
//    	restPedelecMockMvc.perform(post("/app/rest/pedelecs")
//    			.contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .content(TestUtil.convertObjectToJsonBytes(pedelec)))
//                .andExpect(status().isOk());
//
//    	// Read updated Pedelec
//    	restPedelecMockMvc.perform(get("/app/rest/pedelecs/{id}", DEFAULT_ID))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
//    			.andExpect(jsonPath("$.sampleDateAttribute").value(UPD_SAMPLE_DATE_ATTR.toString()))
//    			.andExpect(jsonPath("$.sampleTextAttribute").value(UPD_SAMPLE_TEXT_ATTR));
//
//    	// Delete Pedelec
//    	restPedelecMockMvc.perform(delete("/app/rest/pedelecs/{id}", DEFAULT_ID)
//                .accept(TestUtil.APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk());
//
//    	// Read nonexisting Pedelec
//    	restPedelecMockMvc.perform(get("/app/rest/pedelecs/{id}", DEFAULT_ID)
//                .accept(TestUtil.APPLICATION_JSON_UTF8))
//                .andExpect(status().isNotFound());
//
//    }
}
