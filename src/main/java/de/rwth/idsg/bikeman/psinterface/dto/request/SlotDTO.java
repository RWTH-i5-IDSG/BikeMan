package de.rwth.idsg.bikeman.psinterface.dto.request;

import de.rwth.idsg.bikeman.psinterface.dto.OperationState;
import lombok.Data;

/**
 * Created by swam on 31/07/14.
 */
public class SlotDTO {

    @Data
    public static class Boot {
        private String slotManufacturerId;
        private Integer slotPosition;
        private String pedelecManufacturerId;
    }

    @Data
    public static class StationStatus {
        private String slotManufacturerId;
        private String slotErrorCode;
        private String slotErrorInfo;
        private OperationState slotState;
    }
}
