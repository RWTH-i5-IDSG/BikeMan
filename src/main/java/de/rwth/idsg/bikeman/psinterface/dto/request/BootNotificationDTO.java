package de.rwth.idsg.bikeman.psinterface.dto.request;

import lombok.Data;

import java.util.List;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class BootNotificationDTO {
    private String stationManufacturerId;
    private String firmwareVersion;
    private String stationURL;
    private List<SlotDTO> slots;
}
