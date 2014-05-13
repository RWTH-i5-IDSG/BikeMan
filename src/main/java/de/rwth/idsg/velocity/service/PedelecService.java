package de.rwth.idsg.velocity.service;

import de.rwth.idsg.velocity.domain.Pedelec;
import de.rwth.idsg.velocity.repository.PedelecRepository;
import de.rwth.idsg.velocity.web.rest.dto.PedelecDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by max on 08/05/14.
 */
@Service
@Transactional
public class PedelecService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    private PedelecRepository pedelecRepository;

    public void createPedelec(PedelecDTO pedelec) {
        Pedelec newPedelec = new Pedelec();

        newPedelec.setState(true);
        newPedelec.setStateOfCharge(1.0f);
        newPedelec.setPedelecId(pedelec.getPedelecId());

        pedelecRepository.save(newPedelec);

        log.debug("Created new Pedelec {}", newPedelec);
    }
}
