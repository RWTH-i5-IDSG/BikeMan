package de.rwth.idsg.bikeman.psinterface.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.rwth.idsg.bikeman.psinterface.UnixTimestampDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

/**
 * Created by swam on 31/07/14.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StopTransactionDTO {
    private String pedelecManufacturerId;
    private String stationManufacturerId;
    private String slotManufacturerId;

    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    private DateTime timestamp;
}
