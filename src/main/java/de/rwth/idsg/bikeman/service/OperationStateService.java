package de.rwth.idsg.bikeman.service;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.domain.Pedelec;
import de.rwth.idsg.bikeman.domain.Station;
import de.rwth.idsg.bikeman.domain.StationSlot;
import de.rwth.idsg.bikeman.ixsi.service.AvailabilityPushService;
import de.rwth.idsg.bikeman.psinterface.Utils;
import de.rwth.idsg.bikeman.psinterface.dto.OperationState;
import de.rwth.idsg.bikeman.psinterface.dto.request.PedelecStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.SlotDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StationStatusDTO;
import de.rwth.idsg.bikeman.repository.PedelecRepository;
import de.rwth.idsg.bikeman.repository.StationRepository;
import de.rwth.idsg.bikeman.repository.StationSlotRepository;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xjc.schema.ixsi.TimePeriodType;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Wolfgang Kluth on 03/09/15.
 */

@Service
@Transactional
public class OperationStateService {

    @Inject private PedelecRepository pedelecRepository;
    @Inject private StationRepository stationRepository;
    @Inject private StationSlotRepository stationSlotRepository;
    @Inject private AvailabilityPushService availabilityPushService;

    // -------------------------------------------------------------------------
    // PUSH INAVAILABILITY
    // -------------------------------------------------------------------------

    public void pushStationInavailability(String stationManufacturerId) {
        List<String> pedelecManufacturerIds = pedelecRepository.findManufacturerIdsByStation(stationManufacturerId);

        pushInavailability(stationManufacturerId, pedelecManufacturerIds);
    }

    public void pushSlotInavailability(String stationManufacturerId, String slotManufacturerId) {
        Optional<Pedelec> pedelec = pedelecRepository.findPedelecsByStationSlot(stationManufacturerId, slotManufacturerId);

        if (pedelec.isPresent()) {
            pushInavailability(pedelec.get());
        }
    }

    public void pushPedelecInavailability(String pedelecManufacturerId) {
        Pedelec pedelec = pedelecRepository.findByManufacturerId(pedelecManufacturerId);

        if (pedelec.getStationSlot() != null) {
            pushInavailability(pedelec);
        }
    }

    public void pushInavailability(StationStatusDTO dto) {
        if (isInoperative(dto.getStationState())) {
            pushStationInavailability(dto.getStationManufacturerId());
            return;
        }

        if (Utils.isEmpty(dto.getSlots())) {
            return;
        }

        // For all inoperative slots, if there is a pedelec, get it's id
        List<String> pedelecManufacturerIds =
                dto.getSlots()
                   .parallelStream()
                   .filter(s -> isInoperative(s.getSlotState()))
                   .map(s -> pedelecRepository.findPedelecsByStationSlot(dto.getStationManufacturerId(), s.getSlotManufacturerId()))
                   .filter(Optional::isPresent)
                   .map(s -> s.get().getManufacturerId())
                   .collect(Collectors.toList());

        pushInavailability(dto.getStationManufacturerId(), pedelecManufacturerIds);
    }

    public void pushInavailability(PedelecStatusDTO dto) {
        if (isInoperative(dto.getPedelecState())) {
            pushPedelecInavailability(dto.getPedelecManufacturerId());
        }
    }

    // -------------------------------------------------------------------------
    // PUSH AVAILABILITY
    // -------------------------------------------------------------------------

    public void pushStationAvailability(String stationManufacturerId) {
        List<Pedelec> pedelecs = pedelecRepository.findByStation(stationManufacturerId);

        List<String> pedelecManufacturerIds =
                pedelecs.parallelStream()
                        .filter(this::shouldSendAvailability)
                        .map(Pedelec::getManufacturerId)
                        .collect(Collectors.toList());

        pushAvailability(stationManufacturerId, pedelecManufacturerIds);
    }

    public void pushSlotAvailability(String stationManufacturerId, String slotManufacturerId) {
        Optional<Pedelec> pedelec = pedelecRepository.findPedelecsByStationSlot(stationManufacturerId, slotManufacturerId);

        if (pedelec.isPresent() && shouldSendAvailability(pedelec.get())) {
            pushAvailability(pedelec.get());
        }
    }

    public void pushPedelecAvailability(String pedelecManufacturerId) {
        Pedelec pedelec = pedelecRepository.findByManufacturerId(pedelecManufacturerId);

        if (shouldSendAvailability(pedelec)) {
            pushAvailability(pedelec);
        }
    }

    public void pushAvailability(StationStatusDTO dto) {
        Station station = stationRepository.findByManufacturerId(dto.getStationManufacturerId());

        if (becameOperative(station.getState(), dto.getStationState())) {
            pushStationAvailability(station.getManufacturerId());
            return;
        }

        if (Utils.isEmpty(dto.getSlots())) {
            return;
        }

        // For all slots, that changed from inoperative to operative, if there is an operative pedelec, get it's id
        List<String> pedelecManufacturerIds =
                dto.getSlots()
                   .parallelStream()
                   .map(s -> getPedelecToPush(station, s))
                   .filter(Optional::isPresent)
                   .map(p -> p.get().getManufacturerId())
                   .collect(Collectors.toList());

        pushAvailability(dto.getStationManufacturerId(), pedelecManufacturerIds);
    }

    public void pushAvailability(PedelecStatusDTO dto) {
        if (isOperative(dto.getPedelecState())) {
            pushPedelecAvailability(dto.getPedelecManufacturerId());
        }
    }

    // -------------------------------------------------------------------------
    // Private helpers, mainly for DRY
    // -------------------------------------------------------------------------

    private boolean shouldSendAvailability(Pedelec p) {
        return p.getStationSlot() != null
                && isOperative(p.getState())
                && isOperative(p.getStationSlot().getState())
                && isOperative(p.getStationSlot().getStation().getState());
    }

    private boolean shouldSendAvailability(StationSlot slot, SlotDTO.StationStatus dto) {
        return slot.getPedelec() != null
                && becameOperative(slot.getState(), dto.getSlotState())
                && isOperative(slot.getPedelec().getState());
    }

    private boolean becameOperative(de.rwth.idsg.bikeman.domain.OperationState oldState, OperationState newState) {
        return !isOperative(oldState) && isOperative(newState);
    }

    private boolean isOperative(de.rwth.idsg.bikeman.domain.OperationState os) {
        return os == de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE;
    }

    private boolean isOperative(OperationState os) {
        return os == OperationState.OPERATIVE;
    }

    private boolean isInoperative(OperationState os) {
        return os == OperationState.INOPERATIVE;
    }

    private Optional<Pedelec> getPedelecToPush(Station station, SlotDTO.StationStatus dto) {
        StationSlot slot = stationSlotRepository.findByManufacturerId(dto.getSlotManufacturerId(), station.getManufacturerId());

        if (shouldSendAvailability(slot, dto)) {
            return Optional.of(slot.getPedelec());
        } else {
            return Optional.absent();
        }
    }

    private void pushAvailability(Pedelec pedelec) {
        pushAvailability(
                pedelec.getStationSlot().getStation().getManufacturerId(),
                Collections.singletonList(pedelec.getManufacturerId())
        );
    }

    private void pushInavailability(Pedelec pedelec) {
        pushInavailability(
                pedelec.getStationSlot().getStation().getManufacturerId(),
                Collections.singletonList(pedelec.getManufacturerId())
        );
    }

    private TimePeriodType buildTimePeriod() {
        DateTime now = DateTime.now();

        return new TimePeriodType()
                .withBegin(now)
                .withEnd(now.plusDays(90));
    }

    // -------------------------------------------------------------------------
    // Main methods for IXSI calls
    //
    // TODO: instead of pushing each pedelec on it's own, push a list
    // -------------------------------------------------------------------------

    private void pushInavailability(String stationManufacturerId, List<String> idList) {
        if (Utils.isEmpty(idList)) {
            return;
        }

        TimePeriodType timePeriodType = buildTimePeriod();

        idList.parallelStream()
              .forEach(s -> availabilityPushService.placedBooking(s, stationManufacturerId, timePeriodType));
    }

    private void pushAvailability(String stationManufacturerId, List<String> idList) {
        if (Utils.isEmpty(idList)) {
            return;
        }

        TimePeriodType timePeriodType = buildTimePeriod();

        idList.parallelStream()
              .forEach(s -> availabilityPushService.cancelledBooking(s, stationManufacturerId, timePeriodType));
    }
}
