package de.rwth.idsg.bikeman.service;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.domain.Pedelec;
import de.rwth.idsg.bikeman.domain.StationSlot;
import de.rwth.idsg.bikeman.ixsi.service.AvailabilityPushService;
import de.rwth.idsg.bikeman.ixsi.service.PlaceAvailabilityPushService;
import de.rwth.idsg.bikeman.psinterface.Utils;
import de.rwth.idsg.bikeman.repository.PedelecRepository;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xjc.schema.ixsi.TimePeriodType;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

/**
 * Created by Wolfgang Kluth on 03/09/15.
 */

@Service
@Transactional
public class OperationStateService {

    @Inject private PedelecRepository pedelecRepository;

    @Inject private AvailabilityPushService availabilityPushService;
    @Inject private PlaceAvailabilityPushService placeAvailabilityPushService;

    public void pushStationChange(String manufacturerId, OperationState state) {
        switch (state) {
            case OPERATIVE:
                pushStationAvailability(manufacturerId);
                break;

            case INOPERATIVE:
                pushStationInavailability(manufacturerId);
                break;

            case DELETED:
                pushStationDeletion(manufacturerId);
                break;

            default:
                throw new RuntimeException("Unexpected state");
        }
    }

    public void pushSlotChange(StationSlot slot) {
        switch (slot.getState()) {
            case OPERATIVE:
                pushSlotAvailability(slot.getStation().getManufacturerId(), slot.getManufacturerId());
                break;

            case INOPERATIVE:
                pushSlotInavailability(slot.getStation().getManufacturerId(), slot.getManufacturerId());
                break;

            case DELETED:
                pushSlotDeletion(slot.getStation().getManufacturerId(), slot.getManufacturerId());
                break;

            default:
                throw new RuntimeException("Unexpected state");
        }
    }

    public void pushPedelecChange(String manufacturerId, OperationState state) {
        switch (state) {
            case OPERATIVE:
                pushPedelecAvailability(manufacturerId);
                break;

            case INOPERATIVE:
                pushPedelecInavailability(manufacturerId);
                break;

            case DELETED:
                pushPedelecDeletion(manufacturerId);
                break;

            default:
                throw new RuntimeException("Unexpected state");
        }
    }

    // -------------------------------------------------------------------------
    // Station
    // -------------------------------------------------------------------------

    private void pushStationAvailability(String stationManufacturerId) {
        placeAvailabilityPushService.reportChange(stationManufacturerId);

        pedelecRepository.findByStation(stationManufacturerId)
                         .forEach(this::pushPedelecAvailability);
    }

    private void pushStationInavailability(String stationManufacturerId) {
        placeAvailabilityPushService.reportChange(stationManufacturerId);

        List<String> pedelecManufacturerIds = pedelecRepository.findManufacturerIdsByStation(stationManufacturerId);

        pushPedelecInavailability(stationManufacturerId, pedelecManufacturerIds);
    }

    private void pushStationDeletion(String stationManufacturerId) {
        throw new RuntimeException("Not implemented");
    }

    // -------------------------------------------------------------------------
    // Slot
    // -------------------------------------------------------------------------

    private void pushSlotAvailability(String stationManufacturerId, String slotManufacturerId) {
        placeAvailabilityPushService.reportChange(stationManufacturerId);

        Optional<Pedelec> pedelec = pedelecRepository.findPedelecsByStationSlot(stationManufacturerId, slotManufacturerId);
        if (pedelec.isPresent()) {
            pushPedelecAvailability(pedelec.get());
        }
    }

    private void pushSlotInavailability(String stationManufacturerId, String slotManufacturerId) {
        placeAvailabilityPushService.reportChange(stationManufacturerId);

        Optional<Pedelec> pedelec = pedelecRepository.findPedelecsByStationSlot(stationManufacturerId, slotManufacturerId);
        if (pedelec.isPresent()) {
            pushPedelecInavailability(pedelec.get());
        }
    }

    private void pushSlotDeletion(String stationManufacturerId, String slotManufacturerId) {
        throw new RuntimeException("Not implemented");
    }

    // -------------------------------------------------------------------------
    // Pedelec
    // -------------------------------------------------------------------------

    public void pushPedelecAvailability(String pedelecManufacturerId) {
        Pedelec pedelec = pedelecRepository.findByManufacturerId(pedelecManufacturerId);
        pushPedelecAvailability(pedelec);
    }

    public void pushPedelecInavailability(String pedelecManufacturerId) {
        Pedelec pedelec = pedelecRepository.findByManufacturerId(pedelecManufacturerId);
        pushPedelecInavailability(pedelec);
    }

    private void pushPedelecDeletion(String manufacturerId) {
        throw new RuntimeException("Not implemented");
    }

    // -------------------------------------------------------------------------
    // Private helpers, mainly for DRY
    // -------------------------------------------------------------------------

    private boolean shouldSendAvailability(Pedelec p) {
        return p.getStationSlot() != null
                && p.getStationSlot().getState() == OperationState.OPERATIVE
                && p.getStationSlot().getStation().getState() == OperationState.OPERATIVE;
    }

    private void pushPedelecAvailability(Pedelec pedelec) {
        if (!shouldSendAvailability(pedelec)) {
            return;
        }

        pushPedelecAvailability(
                pedelec.getStationSlot().getStation().getManufacturerId(),
                Collections.singletonList(pedelec.getManufacturerId())
        );
    }

    private void pushPedelecInavailability(Pedelec pedelec) {
        pushPedelecInavailability(
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

    private void pushPedelecInavailability(String stationManufacturerId, List<String> pedelecIdList) {
        if (Utils.isEmpty(pedelecIdList)) {
            return;
        }

        TimePeriodType timePeriodType = buildTimePeriod();

        pedelecIdList.forEach(s -> availabilityPushService.buildAndSend(s, stationManufacturerId, timePeriodType, false));
    }

    private void pushPedelecAvailability(String stationManufacturerId, List<String> pedelecIdList) {
        if (Utils.isEmpty(pedelecIdList)) {
            return;
        }

        TimePeriodType timePeriodType = buildTimePeriod();

        pedelecIdList.forEach(s -> availabilityPushService.buildAndSend(s, stationManufacturerId, timePeriodType, true));
    }
}
