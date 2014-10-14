package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.Application;
import de.rwth.idsg.bikeman.psinterface.dto.request.StartTransactionDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StopTransactionDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewTransactionDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Date;
import java.util.List;

/**
 * Created by swam on 13/10/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("dev")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

@Slf4j
public class TransactionRepositoryTest {

    @Inject
    private TransactionRepository transactionRepository;

    @Test
    public void test1_transactionRepository() {

        try {
            log.info("Fetch All Transactions.");
            List<ViewTransactionDTO> list = transactionRepository.findAll();
            log.info("All fetched transactions: {}", list);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2_startTransaction() {

        StartTransactionDTO startTransactionDTO = StartTransactionDTO
                .builder()
                .cardId("123")
                .pedelecManufacturerId("jjjkljkl")
                .slotManufacturerId("ssdfsdf")
                .stationManufacturerId("yukhkjh")
                .timestamp(new Date().getTime())
                .build();

        try {
            log.info("Going to start Transactions with CardId {} and PedelecManuId {} from StationManuId {}.", startTransactionDTO.getCardId(), startTransactionDTO.getPedelecManufacturerId(), startTransactionDTO.getStationManufacturerId());
            transactionRepository.start(startTransactionDTO);
            log.info("Started transaction");
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3_stopTransaction() {

        StopTransactionDTO stopTransactionDTO = StopTransactionDTO
                .builder()
                .pedelecManufacturerId("jjjkljkl")
                .slotManufacturerId("ssdfsdf")
                .stationManufacturerId("yukhkjh")
                .timestamp(new Date().getTime())
                .build();

        try {
            log.info("Going to stop Transactions with Customer {} and Pedelec {} from Station {}.");
            transactionRepository.stop(stopTransactionDTO);
            log.info("Stoped transaction");
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

}
