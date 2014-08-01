package de.rwth.idsg.velocity.psinterface.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by swam on 31/07/14.
 */

@Getter
@ToString(includeFieldNames = true)
public class BootNotificationDTO {
    private String stationManufacturerId;
    private String firmwareVersion;
    @Setter
    private List<SlotDTO> slotDTOs;

    public BootNotificationDTO(String stationManufacturerId, String firmwareVersion) {
        this.stationManufacturerId = stationManufacturerId;
        this.firmwareVersion = firmwareVersion;
    }
}
