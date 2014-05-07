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
import de.rwth.idsg.velocity.domain.StationSlot;
import de.rwth.idsg.velocity.repository.StationSlotRepository;


/**
 * Test class for the StationSlotResource REST controller.
 *
 * @see StationSlotResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class })
@ActiveProfiles("dev")
public class StationSlotResourceTest {
    
    private static final Long DEFAULT_ID = new Long(1L);

    private static final LocalDate DEFAULT_SAMPLE_DATE_ATTR = new LocalDate(0L);

    private static final LocalDate UPD_SAMPLE_DATE_ATTR = new LocalDate();

    private static final String DEFAULT_SAMPLE_TEXT_ATTR = "sampleTextAttribute";

    private static final String UPD_SAMPLE_TEXT_ATTR = "sampleTextAttributeUpt";

    @Inject
    private StationSlotRepository stationslotRepository;

    private MockMvc restStationSlotMockMvc;
    
    private StationSlot stationslot;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StationSlotResource stationslotResource = new StationSlotResource();
        ReflectionTestUtils.setField(stationslotResource, "stationslotRepository", stationslotRepository);

        this.restStationSlotMockMvc = MockMvcBuilders.standaloneSetup(stationslotResource).build();

        stationslot = new StationSlot();
//        stationslot.setId(DEFAULT_ID);
//    	stationslot.setSampleDateAttribute(DEFAULT_SAMPLE_DATE_ATTR);
//    	stationslot.setSampleTextAttribute(DEFAULT_SAMPLE_TEXT_ATTR);
    }

    @Test
    public void testCRUDStationSlot() throws Exception {

    	// Create StationSlot
    	restStationSlotMockMvc.perform(post("/app/rest/stationslots")
    			.contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stationslot)))
                .andExpect(status().isOk());

    	// Read StationSlot
    	restStationSlotMockMvc.perform(get("/app/rest/stationslots/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
    			.andExpect(jsonPath("$.sampleDateAttribute").value(DEFAULT_SAMPLE_DATE_ATTR.toString()))
    			.andExpect(jsonPath("$.sampleTextAttribute").value(DEFAULT_SAMPLE_TEXT_ATTR));

    	// Update StationSlot
//    	stationslot.setSampleDateAttribute(UPD_SAMPLE_DATE_ATTR);
//    	stationslot.setSampleTextAttribute(UPD_SAMPLE_TEXT_ATTR);
  
    	restStationSlotMockMvc.perform(post("/app/rest/stationslots")
    			.contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stationslot)))
                .andExpect(status().isOk());

    	// Read updated StationSlot
    	restStationSlotMockMvc.perform(get("/app/rest/stationslots/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
    			.andExpect(jsonPath("$.sampleDateAttribute").value(UPD_SAMPLE_DATE_ATTR.toString()))
    			.andExpect(jsonPath("$.sampleTextAttribute").value(UPD_SAMPLE_TEXT_ATTR));

    	// Delete StationSlot
    	restStationSlotMockMvc.perform(delete("/app/rest/stationslots/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

    	// Read nonexisting StationSlot
    	restStationSlotMockMvc.perform(get("/app/rest/stationslots/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

    }
}
