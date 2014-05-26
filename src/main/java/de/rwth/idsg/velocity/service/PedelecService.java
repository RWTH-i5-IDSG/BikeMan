package de.rwth.idsg.velocity.service;

import de.rwth.idsg.velocity.domain.Pedelec;
import de.rwth.idsg.velocity.repository.PedelecRepository;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditPedelecDTO;
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

    private static final Logger log = LoggerFactory.getLogger(PedelecService.class);

    @Inject
    private PedelecRepository pedelecRepository;

    public void createPedelec(CreateEditPedelecDTO dto) {
//
//        Pedelec pedelec = new Pedelec();
//        pedelec.setState(dto.getState());
//        pedelec.setStateOfCharge(1.0f);
//        pedelec.setManufacturerId(dto.getManufacturerId());
//
//        pedelecRepository.create(pedelec);
//
//        log.debug("Created new Pedelec {}", pedelec);
    }
}