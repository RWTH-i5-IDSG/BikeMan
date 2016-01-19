package de.rwth.idsg.bikeman.app.service;

import de.rwth.idsg.bikeman.app.dto.ViewPedelecSlotDTO;
import de.rwth.idsg.bikeman.app.exception.AppErrorCode;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.domain.Pedelec;
import de.rwth.idsg.bikeman.app.repository.PedelecRepository;
import de.rwth.idsg.bikeman.domain.Station;
import de.rwth.idsg.bikeman.repository.StationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Service("PedelecServiceApp")
@Slf4j
public class PedelecService {

    @Autowired
    private PedelecRepository pedelecRepository;

    @Autowired
    private StationRepository stationRepository;

    public Optional<ViewPedelecSlotDTO> getRecommendedPedelec(Long stationId) {
        try {
            stationRepository.findOne(stationId);
        } catch (NoResultException e) {
            throw new AppException("Station not found!", AppErrorCode.CONSTRAINT_FAILED);
        }

        List<Pedelec> pedelecs =  pedelecRepository.findAvailablePedelecs(stationId);

        if (pedelecs.isEmpty()) {
            return Optional.empty();
        }

        // get Pedelec with max. SOC
        Pedelec pedelec = pedelecs.get(0);

        return Optional.of(
                ViewPedelecSlotDTO.builder()
                    .stationSlotId(pedelec.getStationSlot().getStationSlotId())
                    .stationSlotPosition(pedelec.getStationSlot().getStationSlotPosition())
                    .build()
                );

    }

    public Optional<Long> getRecommendedPedelecSlotId(Long stationId) {
        try {
            stationRepository.findOne(stationId);
        } catch (NoResultException e) {
            throw new AppException("Station not found!", AppErrorCode.CONSTRAINT_FAILED);
        }

        List<Pedelec> pedelecs =  pedelecRepository.findAvailablePedelecs(stationId);

        if (pedelecs.isEmpty()) {
            return Optional.empty();
        }

        // get Pedelec with max. SOC
        Pedelec pedelec = pedelecs.get(0);

        return Optional.of(pedelec.getStationSlot().getStationSlotId());
    }

    public Pedelec getRecommendedPedelecForBooking(Long stationId) {
        try {
            stationRepository.findOne(stationId);
        } catch (NoResultException e) {
            throw new AppException("Station not found!", AppErrorCode.CONSTRAINT_FAILED);
        }

        List<Pedelec> pedelecs = pedelecRepository.findAvailablePedelecs(stationId);

        if (pedelecs.isEmpty()) {
            return null;
        }

        // TODO: currently the pedelec with max. SOC is selected --> switch to e.g. the third fullest, cause there is
        // still some time (15min) left to charge it
        return pedelecs.get(0);
    }

}
