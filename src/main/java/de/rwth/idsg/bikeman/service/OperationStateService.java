package de.rwth.idsg.bikeman.service;

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
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xjc.schema.ixsi.TimePeriodType;

import javax.inject.Inject;
import javax.persistence.NoResultException;
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

        List<Pedelec> pedelecs = pedelecRepository.findPedelecsByStationSlot(slotManufacturerId);

        if (Utils.isEmpty(pedelecs)) {
            return;
        }

        Pedelec pedelec = pedelecs.get(0);

        pedelecManufacturerIds.add(pedelec.getManufacturerId());

        String stationManufacturerId = pedelec.getStationSlot().getStation().getManufacturerId();

        pushInavailability(stationManufacturerId, pedelecManufacturerIds);
    }

    public void pushPedelecInavailability(String pedelecManufacturerId) {
        List<String> pedelecManufacturerIds = new ArrayList<>();

        Pedelec pedelec = pedelecRepository.findByManufacturerId(pedelecManufacturerId);

        if (pedelec.getStationSlot() == null) {
            return;
        }

        pedelecManufacturerIds.add(pedelec.getManufacturerId());

        String stationManufacturerId = pedelec.getStationSlot().getStation().getManufacturerId();

        pushInavailability(stationManufacturerId, pedelecManufacturerIds);
    }


    public void pushInavailability(StationStatusDTO stationStatusDTO) {
        List<String> pedelecManufacturerIds = new ArrayList<>();

        if (stationStatusDTO.getStationState() == OperationState.INOPERATIVE) {
            pedelecManufacturerIds = pedelecRepository.findManufacturerIdsByStation(stationStatusDTO.getStationManufacturerId());
        } else if (!Utils.isEmpty(stationStatusDTO.getSlots())) {
            for (SlotDTO.StationStatus slotStatus : stationStatusDTO.getSlots()) {
                if (slotStatus.getSlotState() == OperationState.INOPERATIVE) {

                    List<Pedelec> pedelecs = pedelecRepository.findPedelecsByStationSlot(slotStatus.getSlotManufacturerId());

                    if (Utils.isEmpty(pedelecs)) {
                        return;
                    }

                    Pedelec pedelec = pedelecs.get(0);

                    if (pedelec != null) {
                        pedelecManufacturerIds.add(pedelec.getManufacturerId());
                    }
                }
            }
        }

        pushInavailability(stationStatusDTO.getStationManufacturerId(), pedelecManufacturerIds);
    }

    public void pushInavailability(PedelecStatusDTO pedelecStatus) {
        List<String> pedelecManufacturerIds = new ArrayList<>();

        Pedelec pedelec = pedelecRepository.findByManufacturerId(pedelecStatus.getPedelecManufacturerId());

        if (pedelec.getStationSlot() == null) {
            return;
        }

        if (pedelecStatus.getPedelecState() == OperationState.INOPERATIVE) {
            pedelecManufacturerIds.add(pedelecStatus.getPedelecManufacturerId());
        }

        String stationManufacturerId = pedelec.getStationSlot().getStation().getManufacturerId();

        pushInavailability(stationManufacturerId, pedelecManufacturerIds);
    }


    // TODO: push list of inavailabilities
    private void pushInavailability(String stationManufacturerId, List<String> pedelecManufacturerIds) {

        if (Utils.isEmpty(pedelecManufacturerIds)) {
            return;
        }

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
        List<Pedelec> pedelecs = pedelecRepository.findByStation(stationManufacturerId);

        List<String> pedelecManufacturerIds = new ArrayList<>(pedelecs.size());

        for (Pedelec pedelec : pedelecs) {
            boolean pedelecIsOperative = (pedelec.getState() == de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE);
            boolean slotIsOperative = (pedelec.getStationSlot().getState() == de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE);

            if (!(pedelecIsOperative && slotIsOperative)) {
                continue;
            }

            pedelecManufacturerIds.add(pedelec.getManufacturerId());
        }

        pushAvailability(stationManufacturerId, pedelecManufacturerIds);
    }

    public void pushSlotAvailability(String slotManufacturerId) {

        List<String> pedelecManufacturerIds = new ArrayList<>();

        List<Pedelec> pedelecs = pedelecRepository.findPedelecsByStationSlot(slotManufacturerId);

        if (Utils.isEmpty(pedelecs)) {
            return;
        }

        Pedelec pedelec = pedelecs.get(0);

        Station station = pedelec.getStationSlot().getStation();

        boolean stationIsOperative = (station.getState() == de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE);

        if (!stationIsOperative) {
            return;
        }

        boolean pedelecIsOperative = (pedelec.getState() == de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE);
        boolean slotIsOperative = (pedelec.getStationSlot().getState() == de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE);

        if (!(pedelecIsOperative && slotIsOperative)) {
            return;
        }

        pedelecManufacturerIds.add(pedelec.getManufacturerId());

        String stationManufacturerId = pedelec.getStationSlot().getStation().getManufacturerId();

        pushAvailability(stationManufacturerId, pedelecManufacturerIds);
    }

    public void pushPedelecAvailability(String pedelecManufacturerId) {
        List<String> pedelecManufacturerIds = new ArrayList<>();

        Pedelec pedelec = pedelecRepository.findByManufacturerId(pedelecManufacturerId);

        if (pedelec.getStationSlot() == null) {
            return;
        }

        Station station = pedelec.getStationSlot().getStation();

        boolean stationIsOperative = (station.getState() == de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE);

        if (!stationIsOperative) {
            return;
        }

        boolean slotIsOperative = (pedelec.getStationSlot().getState() == de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE);

        if (!slotIsOperative) {
            return;
        }

        boolean pedelecIsOperative = (pedelec.getState() == de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE);

        if (!pedelecIsOperative) {
            return;
        }

        pedelecManufacturerIds.add(pedelec.getManufacturerId());

        String stationManufacturerId = pedelec.getStationSlot().getStation().getManufacturerId();

        pushAvailability(stationManufacturerId, pedelecManufacturerIds);
    }

    public void pushAvailability(StationStatusDTO stationStatusDTO) {
        List<String> pedelecManufacturerIds = new ArrayList<>();

        Station station = stationRepository.findByManufacturerId(stationStatusDTO.getStationManufacturerId());
        boolean stationWasInoperative = (station.getState() != de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE);

        boolean stationIsOperative = (stationStatusDTO.getStationState() == OperationState.OPERATIVE);

        if (stationIsOperative && stationWasInoperative) {
            List<Pedelec> pedelecs = pedelecRepository.findByStation(stationStatusDTO.getStationManufacturerId());

            for (Pedelec pedelec : pedelecs) {

                boolean pedelecIsOperative = (pedelec.getState() == de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE);
                boolean slotIsOperative = (pedelec.getStationSlot().getState() == de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE);

                if (pedelecIsOperative && slotIsOperative) {
                    pedelecManufacturerIds.add(pedelec.getManufacturerId());
                }
            }
        } else if (!Utils.isEmpty(stationStatusDTO.getSlots())) {
            for (SlotDTO.StationStatus slotStatus : stationStatusDTO.getSlots()) {

                StationSlot slot = stationSlotRepository.findByManufacturerId(slotStatus.getSlotManufacturerId(), station.getManufacturerId());

                if (slot.getPedelec() == null) {
                    continue;
                }

                boolean slotWasInoperative = (slot.getState() != de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE);
                boolean slotIsOperative = (slotStatus.getSlotState() == OperationState.OPERATIVE);

                if (slotIsOperative && slotWasInoperative) {
                    Pedelec pedelec = pedelecRepository.findByStationSlot(slotStatus.getSlotManufacturerId());

                    boolean pedelecIsOperative = (pedelec.getState() == de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE);

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

            boolean slotWasInoperative = (pedelec.getState() != de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE);
            boolean slotIsOperative = (pedelec.getStationSlot().getState() == de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE);

            if (slotIsOperative && (slotWasInoperative || slotWasInoperative)) {
                pedelecManufacturerIds.add(pedelecStatus.getPedelecManufacturerId());
            }
        }

        String stationManufacturerId = pedelec.getStationSlot().getStation().getManufacturerId();

        pushAvailability(stationManufacturerId, pedelecManufacturerIds);
    }

    // TODO: push list of availabilities
    private void pushAvailability(String stationManufacturerId, List<String> pedelecManufacturerIds) {

        if (Utils.isEmpty(pedelecManufacturerIds)) {
            return;
        }

        TimePeriodType timePeriodType = new TimePeriodType()
                .withBegin(DateTime.now())
                .withEnd(DateTime.now().plusDays(90));

        for (String pedelecManufacturerId : pedelecManufacturerIds) {
            availabilityPushService.cancelledBooking(pedelecManufacturerId, stationManufacturerId, timePeriodType);
        }
    }

}
