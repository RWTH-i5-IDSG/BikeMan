package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.Application;
import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.domain.OperationState;
import org.junit.Assert;
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
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by swam on 17/10/14.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("dev")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CardAccountRepositoryTest {

    @Inject
    private CardAccountRepository cardAccountRepository;

    @Test
    public void test1_changeOperationState() {
        List<CardAccount> cardAccountList = cardAccountRepository.findAll();
        CardAccount cardAccount = cardAccountList.get(0);

        int numberChangedRows = cardAccountRepository.setOperationStateFor(OperationState.INOPERATIVE, 6l);

        cardAccount = cardAccountRepository.findOne(cardAccount.getCardAccountId());
        Assert.assertEquals(OperationState.INOPERATIVE, cardAccount.getOperationState());
        Assert.assertEquals(numberChangedRows, 1);
    }

    @Test
    public void test2_readOperationState() {
        CardAccount cardAccount = cardAccountRepository.findOne(6l);

        Assert.assertEquals(OperationState.INOPERATIVE, cardAccount.getOperationState());
    }
}
