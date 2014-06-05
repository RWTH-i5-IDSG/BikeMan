package de.rwth.idsg.velocity.web.rest;

import de.rwth.idsg.velocity.Application;
import de.rwth.idsg.velocity.domain.Address;
import de.rwth.idsg.velocity.domain.OperationState;
import de.rwth.idsg.velocity.repository.StationRepository;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditStationDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewStationDTO;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StationResourceTest {

    private String BASE_PATH = "/app/rest/stations";
    private String ID_PATH = "/app/rest/stations/{id}";
    private int REPEAT_COUNT = 500;
    private Long STATION_ID = 4L;

    @Inject
    private StationRepository stationRepository;

    @Captor
    private ArgumentCaptor<ArrayList<ViewStationDTO>> captor;

    private MockMvc restStationMockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StationResource stationResource = new StationResource();
        ReflectionTestUtils.setField(stationResource, "stationRepository", stationRepository);

        this.restStationMockMvc = MockMvcBuilders.standaloneSetup(stationResource).build();
    }

    @Test
    public void test1_createStation() throws Exception {
        for (int n=1; n <= REPEAT_COUNT; n++ ) {
            Address add = new Address();
            add.setStreetAndHousenumber(RandomStringUtils.randomAlphanumeric(8));
            add.setCity(RandomStringUtils.randomAlphabetic(8));
            add.setCountry(RandomStringUtils.randomAlphabetic(8));
            add.setZip(RandomStringUtils.randomAlphabetic(4));

            CreateEditStationDTO dto = new CreateEditStationDTO();
            dto.setManufacturerId(UUID.randomUUID().toString());
            dto.setName(RandomStringUtils.randomAlphabetic(8));
            dto.setLocationLatitude(new BigDecimal(Math.random()));
            dto.setLocationLongitude(new BigDecimal(Math.random()));
            dto.setName(RandomStringUtils.randomAlphabetic(15));
            dto.setNote(RandomStringUtils.randomAlphabetic(30));
            dto.setState(OperationState.OPERATIVE);
            dto.setAddress(add);

            restStationMockMvc.perform(post(BASE_PATH)
                    .contentType(TestUtil.APPLICATION_JSON_UTF8)
                    .content(TestUtil.convertObjectToJsonBytes(dto)))
                    .andExpect(status().isOk());
        }
    }


    /*
    * Adding stations to DB is not enough. Station slot table must also be filled with
    * values. Otherwise, the result of the method will be an empty list.
    */
    @Ignore
    public void test2_findAllStations() throws Exception {
        restStationMockMvc.perform(get(BASE_PATH))
                .andExpect(status().isOk());
    }

    @Ignore
    public void test3_findOneStation() throws Exception {
        restStationMockMvc.perform(get(ID_PATH, STATION_ID))
                .andExpect(status().isOk());
    }

    @Ignore
    public void test4_updateStation() throws Exception {
        CreateEditStationDTO dto = new CreateEditStationDTO();
        dto.setStationId(1L);
        dto.setManufacturerId("c89df542-af5c-49ca-a99c-53be7dafe59e");
        dto.setNote("blaaaaa");

        restStationMockMvc.perform(put(BASE_PATH)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto)))
                .andExpect(status().isOk());
    }

    @Ignore
    public void test5_deleteStation() throws Exception {
        restStationMockMvc.perform(delete(ID_PATH, STATION_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

//    @Test
//    public void testCRUDStation() throws Exception {
//
//        // Create Station
//        restStationMockMvc.perform(post("/app/rest/stations")
//                .contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .content(TestUtil.convertObjectToJsonBytes(station)))
//                .andExpect(status().isOk());
//
//        // Read Station
//        restStationMockMvc.perform(get("/app/rest/stations/{id}", DEFAULT_ID))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
//                .andExpect(jsonPath("$.sampleDateAttribute").value(DEFAULT_SAMPLE_DATE_ATTR.toString()))
//                .andExpect(jsonPath("$.sampleTextAttribute").value(DEFAULT_SAMPLE_TEXT_ATTR));
//
//        // Update Station
//        station.setLocationLatitude(UPD_SAMPLE_NUMBER_ATTR);
//        station.setLocationLongitude(UPD_SAMPLE_NUMBER_ATTR);
//        station.setName(UPD_SAMPLE_TEXT_ATTR);
//        station.setNote(UPD_SAMPLE_TEXT_ATTR);
//        station.setState(UPD_SAMPLE_BOOLEAN_ATTR);



//        // Read updated Station
//        restStationMockMvc.perform(get("/app/rest/stations/{id}", DEFAULT_ID))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
//                .andExpect(jsonPath("$.locationLatitude").value(UPD_SAMPLE_NUMBER_ATTR.doubleValue()))
//                .andExpect(jsonPath("$.locationLongitude").value(UPD_SAMPLE_NUMBER_ATTR.doubleValue()))
//                .andExpect(jsonPath("$.name").value(UPD_SAMPLE_TEXT_ATTR.toString()))
//                .andExpect(jsonPath("$.note").value(UPD_SAMPLE_TEXT_ATTR.toString()))
//                .andExpect(jsonPath("$.state").value(UPD_SAMPLE_BOOLEAN_ATTR.booleanValue()));
//
//        // Delete Station
//        restStationMockMvc.perform(delete("/app/rest/stations/{id}", DEFAULT_ID)
//                .accept(TestUtil.APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk());
//
//        // Read nonexisting Station
//        restStationMockMvc.perform(get("/app/rest/stations/{id}", DEFAULT_ID)
//                .accept(TestUtil.APPLICATION_JSON_UTF8))
//                .andExpect(status().isNotFound());
//
//        // Set station state
//        restStationMockMvc.perform(post("/app/rest/stations/setState/{id}", DEFAULT_ID)
//                .contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .content(TestUtil.convertObjectToJsonBytes(UPD_SAMPLE_BOOLEAN_ATTR)))
//                .andExpect(status().isOk());
//
//    }

}
