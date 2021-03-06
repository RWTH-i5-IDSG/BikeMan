package de.rwth.idsg.bikeman.psinterface.repository;

import de.rwth.idsg.bikeman.psinterface.dto.request.BootNotificationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StationStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.CardReadKeyDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.CardWriteKeyDTO;

import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 16.06.2015
 */
public interface PsiStationRepository {
    List<CardReadKeyDTO> getCardReadKeys();
    CardWriteKeyDTO getCardWriteKey();
    void updateAfterBoot(BootNotificationDTO dto);
    void updateStationStatus(StationStatusDTO dto);
}
