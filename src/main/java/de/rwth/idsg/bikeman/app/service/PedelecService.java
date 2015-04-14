package de.rwth.idsg.bikeman.app.service;

import de.rwth.idsg.bikeman.app.dto.ViewPedelecSlotDTO;
import de.rwth.idsg.bikeman.domain.Pedelec;
import de.rwth.idsg.bikeman.app.repository.PedelecRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("PedelecServiceApp")
@Slf4j
public class PedelecService {

    @Autowired
    private PedelecRepository pedelecRepository;

    public ViewPedelecSlotDTO getRecommendedPedelec(Long stationId) {
        List<Pedelec> pedelecs =  pedelecRepository.findAvailablePedelecs(stationId);

        if (pedelecs.isEmpty()) {
            return null;
        }

        // get Pedelec with max. SOC
        Pedelec pedelec = pedelecs.get(0);

        return ViewPedelecSlotDTO.builder()
                .stationSlotId(pedelec.getStationSlot().getStationSlotId())
                .stationSlotPosition(pedelec.getStationSlot().getStationSlotPosition())
                .build();

    }

}
