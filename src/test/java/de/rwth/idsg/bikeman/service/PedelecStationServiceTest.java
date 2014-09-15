package de.rwth.idsg.bikeman.service;

import de.rwth.idsg.bikeman.Application;
import de.rwth.idsg.bikeman.psinterface.dto.OperationState;
import de.rwth.idsg.bikeman.psinterface.dto.request.BootNotificationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.SlotDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StartTransactionDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StopTransactionDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.BootConfirmationDTO;
import de.rwth.idsg.bikeman.psinterface.service.PedelecStationService;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 02.09.2014
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("dev")
@Slf4j
public class PedelecStationServiceTest {

    @Inject private PedelecStationService service;

    /**
     * The values are from my DB, so they are not really generic tests
     */
    @Ignore
    public void test1_handleBootNotification() {

        // -------------------------------------------------------------------------
        // Prepare slotDTOs
        // -------------------------------------------------------------------------

        List<OperationState> states = new ArrayList<>();
        Collections.addAll(states, OperationState.values());

        SlotDTO slotDTO1 = new SlotDTO();
        slotDTO1.setSlotManufacturerId("sdfgh");
        slotDTO1.setSlotPosition(1);
        slotDTO1.setPedelecManufacturerId("25d19d2a-7310-4d7c-b2bf-c39bc61753e6");
        slotDTO1.setSlotState(states.get(0));

        SlotDTO slotDTO2 = new SlotDTO();
        slotDTO2.setSlotManufacturerId("asdfghfd");
        slotDTO2.setSlotPosition(2);
        slotDTO2.setPedelecManufacturerId("56dd022f-f6e6-4fe8-8fb5-1d190e78a131");
        slotDTO2.setSlotState(states.get(0));

        List<SlotDTO> slotDTOList = new ArrayList<>();
        slotDTOList.add(slotDTO1);
        slotDTOList.add(slotDTO2);

        // -------------------------------------------------------------------------
        // Prepare BootNotificationDTO
        // -------------------------------------------------------------------------

        BootNotificationDTO bootDTO = new BootNotificationDTO();
        bootDTO.setStationManufacturerId("da76e18e-61e0-4458-b57f-a39ef2dd87a6");
        bootDTO.setFirmwareVersion(RandomStringUtils.randomAlphabetic(4));
        bootDTO.setSlotDTOs(slotDTOList);

        try {
            log.info("Request: {}", bootDTO);
            BootConfirmationDTO confirmDTO = service.handleBootNotification(bootDTO);
            log.info("Response: {}", confirmDTO);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    /**
     * The values are from my DB, so they are not really generic tests
     */
    @Ignore
    public void test2_handleStartTransaction() {
        StartTransactionDTO dto = new StartTransactionDTO();
        dto.setUserId(1L);
        dto.setPedelecManufacturerId("25d19d2a-7310-4d7c-b2bf-c39bc61753e6");
        dto.setStationManufacturerId("da76e18e-61e0-4458-b57f-a39ef2dd87a6");
        dto.setSlotManufacturerId("sdfgh");
        dto.setTimestamp(new DateTime().getMillis());

        try {
            log.info("Request: {}", dto);
            service.handleStartTransaction(dto);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    /**
     * The values are from my DB, so they are not really generic tests
     */
    @Test
    public void test3_handleStopTransaction() {
        StopTransactionDTO dto = new StopTransactionDTO();
        dto.setPedelecManufacturerId("25d19d2a-7310-4d7c-b2bf-c39bc61753e6");
        dto.setTimestamp(new DateTime().getMillis());
        dto.setStationManufacturerId("da76e18e-61e0-4458-b57f-a39ef2dd87a6");
        dto.setSlotManufacturerId("asdfghfd");

        try {
            log.info("Request: {}", dto);
            service.handleStopTransaction(dto);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
}
