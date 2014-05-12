package de.rwth.idsg.velocity.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.inject.Inject;

import de.rwth.idsg.velocity.domain.Address;
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
import de.rwth.idsg.velocity.domain.Station;
import de.rwth.idsg.velocity.repository.StationRepository;

import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * Test class for the StationResource REST controller.
 *
 * @see StationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ActiveProfiles("dev")
public class StationResourceTest {

    private static final Long DEFAULT_ID = new Long(1L);

    private static final LocalDate DEFAULT_SAMPLE_DATE_ATTR = new LocalDate(0L);

    private static final LocalDate UPD_SAMPLE_DATE_ATTR = new LocalDate();

    private static final String DEFAULT_SAMPLE_TEXT_ATTR = "sampleTextAttribute";

    private static final String UPD_SAMPLE_TEXT_ATTR = "sampleTextAttributeUpt";

    private static final BigDecimal DEFAULT_SAMPLE_NUMBER_ATTR = new BigDecimal(0.1234);

    private static final BigDecimal UPD_SAMPLE_NUMBER_ATTR = new BigDecimal(0.4321);

    private static final Boolean DEFAULT_SAMPLE_BOOLEAN_ATTR = true;

    private static final Boolean UPD_SAMPLE_BOOLEAN_ATTR = false;

    @Inject
    private StationRepository stationRepository;

    private MockMvc restStationMockMvc;

    private Station station;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StationResource stationResource = new StationResource();
        ReflectionTestUtils.setField(stationResource, "stationRepository", stationRepository);

        this.restStationMockMvc = MockMvcBuilders.standaloneSetup(stationResource).build();

        station = new Station();
        station.setId(DEFAULT_ID);
        station.setLocationLatitude(DEFAULT_SAMPLE_NUMBER_ATTR);
        station.setLocationLongitude(DEFAULT_SAMPLE_NUMBER_ATTR);
        station.setName(DEFAULT_SAMPLE_TEXT_ATTR);
        station.setNote(DEFAULT_SAMPLE_TEXT_ATTR);

        Address address = new Address();
        address.setStreetAndHousenumber(DEFAULT_SAMPLE_TEXT_ATTR);
        address.setCity(DEFAULT_SAMPLE_TEXT_ATTR);
        address.setCountry(DEFAULT_SAMPLE_TEXT_ATTR);
        address.setId(DEFAULT_ID);
        address.setZip(DEFAULT_SAMPLE_TEXT_ATTR);

        station.setAddress(address);
        station.setState(DEFAULT_SAMPLE_BOOLEAN_ATTR);

    }

    @Test
    public void testCRUDStation() throws Exception {

        // Create Station
        restStationMockMvc.perform(post("/app/rest/stations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(station)))
                .andExpect(status().isOk());

        // Read Station
        restStationMockMvc.perform(get("/app/rest/stations/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
                .andExpect(jsonPath("$.sampleDateAttribute").value(DEFAULT_SAMPLE_DATE_ATTR.toString()))
                .andExpect(jsonPath("$.sampleTextAttribute").value(DEFAULT_SAMPLE_TEXT_ATTR));

        // Update Station
        station.setLocationLatitude(UPD_SAMPLE_NUMBER_ATTR);
        station.setLocationLongitude(UPD_SAMPLE_NUMBER_ATTR);
        station.setName(UPD_SAMPLE_TEXT_ATTR);
        station.setNote(UPD_SAMPLE_TEXT_ATTR);
        station.setState(UPD_SAMPLE_BOOLEAN_ATTR);

        restStationMockMvc.perform(post("/app/rest/stations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(station)))
                .andExpect(status().isOk());

        // Read updated Station
        restStationMockMvc.perform(get("/app/rest/stations/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
                .andExpect(jsonPath("$.locationLatitude").value(UPD_SAMPLE_NUMBER_ATTR.doubleValue()))
                .andExpect(jsonPath("$.locationLongitude").value(UPD_SAMPLE_NUMBER_ATTR.doubleValue()))
                .andExpect(jsonPath("$.name").value(UPD_SAMPLE_TEXT_ATTR.toString()))
                .andExpect(jsonPath("$.note").value(UPD_SAMPLE_TEXT_ATTR.toString()))
                .andExpect(jsonPath("$.state").value(UPD_SAMPLE_BOOLEAN_ATTR.booleanValue()));

        // Delete Station
        restStationMockMvc.perform(delete("/app/rest/stations/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Read nonexisting Station
        restStationMockMvc.perform(get("/app/rest/stations/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

        // Set station state
        restStationMockMvc.perform(post("/app/rest/stations/setState/{id}", DEFAULT_ID)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(UPD_SAMPLE_BOOLEAN_ATTR)))
                .andExpect(status().isOk());

    }
}
