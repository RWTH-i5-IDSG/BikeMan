package de.rwth.idsg.bikeman.psinterface.repository;

import de.rwth.idsg.bikeman.psinterface.dto.request.ChargingStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.PedelecStatusDTO;

import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 16.06.2015
 */
public interface PsiPedelecRepository {
    List<String> findAvailablePedelecs(String stationManufacturerId);
    List<String> findReservedPedelecs(String stationManufacturerId, String cardId);
    void updatePedelecStatus(PedelecStatusDTO dto);
    void updatePedelecChargingStatus(List<ChargingStatusDTO> dtoList);
}
