package de.rwth.idsg.bikeman.psinterface.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.rwth.idsg.bikeman.psinterface.UnixTimestampSerializer;
import lombok.Data;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class BootConfirmationDTO {

    @JsonSerialize(using = UnixTimestampSerializer.class)
    private DateTime timestamp;

    private Integer heartbeatInterval;
    private List<CardReadKeyDTO> cardKeys;
}
