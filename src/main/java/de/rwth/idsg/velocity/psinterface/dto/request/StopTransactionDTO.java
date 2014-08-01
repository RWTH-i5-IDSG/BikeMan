package de.rwth.idsg.velocity.psinterface.dto.request;

import lombok.Data;
import org.joda.time.LocalDateTime;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class StopTransactionDTO {
    private String pedelecManufacturerId;
    private String stationManufacturerId;
    private String slotManufacturerId;
    private LocalDateTime timestamp;
}
