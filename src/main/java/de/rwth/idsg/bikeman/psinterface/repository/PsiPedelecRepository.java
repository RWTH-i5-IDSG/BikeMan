package de.rwth.idsg.bikeman.psinterface.repository;

import de.rwth.idsg.bikeman.psinterface.dto.request.ChargingStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.PedelecStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.AvailablePedelecDTO;

import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 16.06.2015
 */
public interface PsiPedelecRepository {
    List<AvailablePedelecDTO> findAvailablePedelecs(String endpointAddress);
    void updatePedelecStatus(PedelecStatusDTO dto);
    void updatePedelecChargingStatus(List<ChargingStatusDTO> dtoList);
}
