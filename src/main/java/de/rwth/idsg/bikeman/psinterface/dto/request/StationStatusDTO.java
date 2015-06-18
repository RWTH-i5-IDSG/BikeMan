package de.rwth.idsg.bikeman.psinterface.dto.request;

import de.rwth.idsg.bikeman.psinterface.dto.OperationState;
import lombok.Data;

import java.util.List;

/**
 * Created by swam on 31/07/14.
 */
@Data
public class StationStatusDTO {
    private String stationManufacturerId;
    private String stationErrorCode;
    private String stationErrorInfo;
    private OperationState stationState;
    private Long timestamp;
    private List<SlotDTO.StationStatus> slots;
}
