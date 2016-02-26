package de.rwth.idsg.bikeman.app.service;

import de.rwth.idsg.bikeman.app.dto.ViewPedelecSlotDTO;
import de.rwth.idsg.bikeman.app.exception.AppErrorCode;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.app.repository.AppPedelecRepository;
import de.rwth.idsg.bikeman.domain.Pedelec;
import de.rwth.idsg.bikeman.repository.StationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AppPedelecService {

    @Autowired
    private AppPedelecRepository appPedelecRepository;

    @Autowired
    private StationRepository stationRepository;


    // TODO: but why (copy&paste)?
    public Optional<ViewPedelecSlotDTO> getRecommendedPedelec(Long stationId) {
        try {
            stationRepository.findOne(stationId);
        } catch (NoResultException e) {
            throw new AppException("Station not found!", AppErrorCode.CONSTRAINT_FAILED);
        }

        List<Pedelec> pedelecs = appPedelecRepository.findAvailablePedelecs(stationId);

        if (pedelecs.isEmpty()) {
            return Optional.empty();
        }

        // get Pedelec with max. SOC
        // TODO: what if no pedelec is available?
        Pedelec pedelec = pedelecs.get(0);

        return Optional.of(
                ViewPedelecSlotDTO.builder()
                                  .stationSlotId(pedelec.getStationSlot().getStationSlotId())
                                  .stationSlotPosition(pedelec.getStationSlot().getStationSlotPosition())
                                  .build()
        );

    }

    // TODO: but why (copy&paste)?
    public Optional<Long> getRecommendedPedelecSlotId(Long stationId) {
        try {
            stationRepository.findOne(stationId);
        } catch (NoResultException e) {
            throw new AppException("Station not found!", AppErrorCode.CONSTRAINT_FAILED);
        }

        List<Pedelec> pedelecs = appPedelecRepository.findAvailablePedelecs(stationId);

        if (pedelecs.isEmpty()) {
            return Optional.empty();
        }

        // get Pedelec with max. SOC
        // TODO: what if no pedelec is available?
        Pedelec pedelec = pedelecs.get(0);

        return Optional.of(pedelec.getStationSlot().getStationSlotId());
    }

    public Optional<Pedelec> getRecommendedPedelecForBooking(Long stationId) {
        try {
            stationRepository.findOne(stationId);
        } catch (NoResultException e) {
            throw new AppException("Station not found!", AppErrorCode.CONSTRAINT_FAILED);
        }

        List<Pedelec> pedelecs = appPedelecRepository.findAvailablePedelecs(stationId);

        if (pedelecs.isEmpty()) {
            return Optional.empty();
        }

        // TODO: currently the pedelec with max. SOC is selected --> switch to e.g. the third fullest, cause there is
        // still some time (15min) left to charge it
        return Optional.of(pedelecs.get(0));
    }

}
