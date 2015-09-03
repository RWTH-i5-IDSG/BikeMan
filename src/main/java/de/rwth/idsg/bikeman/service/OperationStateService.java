package de.rwth.idsg.bikeman.service;

import de.rwth.idsg.bikeman.domain.Pedelec;
import de.rwth.idsg.bikeman.domain.Station;
import de.rwth.idsg.bikeman.domain.StationSlot;
import de.rwth.idsg.bikeman.ixsi.service.AvailabilityPushService;
import de.rwth.idsg.bikeman.psinterface.dto.OperationState;
import de.rwth.idsg.bikeman.psinterface.dto.request.PedelecStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.SlotDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StationStatusDTO;
import de.rwth.idsg.bikeman.repository.PedelecRepository;
import de.rwth.idsg.bikeman.repository.StationRepository;
import de.rwth.idsg.bikeman.repository.StationSlotRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xjc.schema.ixsi.TimePeriodType;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wolfgang Kluth on 03/09/15.
 */

@Service
@Transactional
@Slf4j
public class OperationStateService {

    @Inject private PedelecRepository pedelecRepository;
    @Inject private StationRepository stationRepository;
    @Inject private StationSlotRepository stationSlotRepository;
    @Inject private AvailabilityPushService availabilityPushService;

    // ==============================
    // ===== PUSH INAVAILABILITY ====
    // ==============================

    public void pushStationInavailability(String stationManufacturerId) {
        List<String> pedelecManufacturerIds = pedelecRepository.findManufacturerIdsByStation(stationManufacturerId);
        pushInavailability(stationManufacturerId, pedelecManufacturerIds);
    }

    public void pushSlotInavailability(String slotManufacturerId) {
        List<String> pedelecManufacturerIds = new ArrayList<>();

        Pedelec pedelec = pedelecRepository.findByStationSlot(slotManufacturerId);
        pedelecManufacturerIds.add(pedelec.getManufacturerId());

        String stationManufacturerId = pedelec.getStationSlot().getStation().getManufacturerId();

        pushInavailability(stationManufacturerId, pedelecManufacturerIds);
    }

    public void pushPedelecInavailability(String pedelecManufacturerId) {
        List<String> pedelecManufacturerIds = new ArrayList<>();

        Pedelec pedelec = pedelecRepository.findByManufacturerId(pedelecManufacturerId);
        pedelecManufacturerIds.add(pedelec.getManufacturerId());

        String stationManufacturerId = pedelec.getStationSlot().getStation().getManufacturerId();

        pushInavailability(stationManufacturerId, pedelecManufacturerIds);
    }


    public void pushInavailability(StationStatusDTO stationStatusDTO) {
        List<String> pedelecManufacturerIds = new ArrayList<>();

        if (stationStatusDTO.getStationState() == OperationState.INOPERATIVE) {
            pedelecManufacturerIds = pedelecRepository.findManufacturerIdsByStation(stationStatusDTO.getStationManufacturerId());
        } else if (!stationStatusDTO.getSlots().isEmpty()) {
            for (SlotDTO.StationStatus slotStatus : stationStatusDTO.getSlots()) {
                if (slotStatus.getSlotState() == OperationState.INOPERATIVE) {
                    Pedelec pedelec = pedelecRepository.findByStationSlot(slotStatus.getSlotManufacturerId());
                    pedelecManufacturerIds.add(pedelec.getManufacturerId());
                }
            }
        }

        pushInavailability(stationStatusDTO.getStationManufacturerId(), pedelecManufacturerIds);
    }

    public void pushInavailability(PedelecStatusDTO pedelecStatus) {
        List<String> pedelecManufacturerIds = new ArrayList<>();

        Pedelec pedelec = pedelecRepository.findByManufacturerId(pedelecStatus.getPedelecManufacturerId());

        if (pedelecStatus.getPedelecState() == OperationState.INOPERATIVE) {
            pedelecManufacturerIds.add(pedelecStatus.getPedelecManufacturerId());
        }

        String stationManufacturerId = pedelec.getStationSlot().getStation().getManufacturerId();

        pushInavailability(stationManufacturerId, pedelecManufacturerIds);
    }

    private void pushInavailability(String stationManufacturerId, List<String> pedelecManufacturerIds) {
        TimePeriodType timePeriodType = new TimePeriodType()
                .withBegin(DateTime.now())
                .withEnd(DateTime.now().plusDays(90));

        for (String pedelecMID : pedelecManufacturerIds) {
            availabilityPushService.placedBooking(pedelecMID, stationManufacturerId, timePeriodType);
        }
    }

    // ==============================
    // ====== PUSH AVAILABILITY =====
    // ==============================

    public void pushStationAvailability(String stationManufacturerId) {
        List<String> pedelecManufacturerIds = pedelecRepository.findManufacturerIdsByStation(stationManufacturerId);
        pushAvailability(stationManufacturerId, pedelecManufacturerIds);
    }

    public void pushSlotAvailability(String slotManufacturerId) {
        List<String> pedelecManufacturerIds = new ArrayList<>();

        Pedelec pedelec = pedelecRepository.findByStationSlot(slotManufacturerId);
        pedelecManufacturerIds.add(pedelec.getManufacturerId());

        String stationManufacturerId = pedelec.getStationSlot().getStation().getManufacturerId();

        pushAvailability(stationManufacturerId, pedelecManufacturerIds);
    }

    public void pushPedelecAvailability(String pedelecManufacturerId) {
        List<String> pedelecManufacturerIds = new ArrayList<>();

        Pedelec pedelec = pedelecRepository.findByManufacturerId(pedelecManufacturerId);
        pedelecManufacturerIds.add(pedelec.getManufacturerId());

        String stationManufacturerId = pedelec.getStationSlot().getStation().getManufacturerId();

        pushAvailability(stationManufacturerId, pedelecManufacturerIds);
    }

    public void pushAvailability(StationStatusDTO stationStatusDTO) {
        List<String> pedelecManufacturerIds = new ArrayList<>();

        Station station = stationRepository.findByManufacturerId(stationStatusDTO.getStationManufacturerId());
        boolean stationWasInoperative = station.getState() == de.rwth.idsg.bikeman.domain.OperationState.INOPERATIVE;

        boolean stationIsOperative = stationStatusDTO.getStationState() == OperationState.OPERATIVE;

        if (stationIsOperative && stationWasInoperative) {
            List<Pedelec> pedelecs = pedelecRepository.findByStation(stationStatusDTO.getStationManufacturerId());

            for (Pedelec pedelec : pedelecs) {

                boolean pedelecIsOperative = (pedelec.getState() == de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE);
                boolean slotIsOperative = (pedelec.getStationSlot().getState() == de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE);

                if (pedelecIsOperative && slotIsOperative) {
                    pedelecManufacturerIds.add(pedelec.getManufacturerId());
                }
            }
        } else if (!stationStatusDTO.getSlots().isEmpty()) {
            for (SlotDTO.StationStatus slotStatus : stationStatusDTO.getSlots()) {

                StationSlot slot = stationSlotRepository.findByManufacturerId(slotStatus.getSlotManufacturerId(), station.getManufacturerId());

                boolean slotWasInoperative = slot.getState() == de.rwth.idsg.bikeman.domain.OperationState.INOPERATIVE;
                boolean slotIsOperative = slotStatus.getSlotState() == OperationState.OPERATIVE;

                if (slotIsOperative && slotWasInoperative) {
                    Pedelec pedelec = pedelecRepository.findByStationSlot(slotStatus.getSlotManufacturerId());

                    boolean pedelecIsOperative = pedelec.getState() == de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE;

                    if (pedelecIsOperative) {
                        pedelecManufacturerIds.add(pedelec.getManufacturerId());
                    }
                }
            }
        }

        pushAvailability(stationStatusDTO.getStationManufacturerId(), pedelecManufacturerIds);
    }

    public void pushAvailability(PedelecStatusDTO pedelecStatus) {
        List<String> pedelecManufacturerIds = new ArrayList<>();

        Pedelec pedelec = pedelecRepository.findByManufacturerId(pedelecStatus.getPedelecManufacturerId());

        if (pedelecStatus.getPedelecState() == OperationState.OPERATIVE) {

            boolean readyToUse = (pedelec.getState() == de.rwth.idsg.bikeman.domain.OperationState.INOPERATIVE);
            boolean slotIsOperative = (pedelec.getStationSlot().getState() == de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE);

            if (readyToUse && slotIsOperative) {
                pedelecManufacturerIds.add(pedelecStatus.getPedelecManufacturerId());
            }
        }

        String stationManufacturerId = pedelec.getStationSlot().getStation().getManufacturerId();

        pushAvailability(stationManufacturerId, pedelecManufacturerIds);
    }

    private void pushAvailability(String stationManufacturerId, List<String> pedelecManufacturerIds) {
        TimePeriodType timePeriodType = new TimePeriodType()
                .withBegin(DateTime.now())
                .withEnd(DateTime.now().plusDays(90));

        for (String pedelecManufacturerId : pedelecManufacturerIds) {
            availabilityPushService.cancelledBooking(pedelecManufacturerId, stationManufacturerId, timePeriodType);
        }
    }

}
