package de.rwth.idsg.bikeman.psinterface.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.rwth.idsg.bikeman.psinterface.UnixTimestampDeserializer;
import de.rwth.idsg.bikeman.psinterface.dto.OperationState;
import lombok.Data;
import org.joda.time.DateTime;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class PedelecStatusDTO {
    private String pedelecManufacturerId;
    private String pedelecErrorCode;
    private String pedelecErrorInfo;
    private OperationState pedelecState;

    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    private DateTime timestamp;
}