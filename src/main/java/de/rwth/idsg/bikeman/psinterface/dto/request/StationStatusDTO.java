package de.rwth.idsg.bikeman.psinterface.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by swam on 31/07/14.
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
public class StationStatusDTO {
    private String stationManufacturerId;
    private String stationErrorCode;
    private String stationInfo;
    private Long timestamp;
    private List<SlotDTO> slots;

    public StationStatusDTO(Long timestamp, String stationInfo, String stationErrorCode, String stationManufacturerId) {
        this.timestamp = timestamp;
        this.stationInfo = stationInfo;
        this.stationErrorCode = stationErrorCode;
        this.stationManufacturerId = stationManufacturerId;
    }
}
