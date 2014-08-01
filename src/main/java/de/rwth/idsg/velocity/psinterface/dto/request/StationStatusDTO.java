package de.rwth.idsg.velocity.psinterface.dto.request;

import de.rwth.idsg.velocity.psinterface.dto.*;
import de.rwth.idsg.velocity.psinterface.dto.Error;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.LocalDateTime;

import java.util.List;

/**
 * Created by swam on 31/07/14.
 */

@Getter
@ToString(includeFieldNames = true)
public class StationStatusDTO {
    private String stationManufacturerId;
    private Error stationErrorCode;
    private String stationInfo;
    private LocalDateTime timestamp;

    @Setter private List<SlotDTO> slots;

    public StationStatusDTO(LocalDateTime timestamp, String stationInfo, Error stationErrorCode, String stationManufacturerId) {
        this.timestamp = timestamp;
        this.stationInfo = stationInfo;
        this.stationErrorCode = stationErrorCode;
        this.stationManufacturerId = stationManufacturerId;
    }
}
