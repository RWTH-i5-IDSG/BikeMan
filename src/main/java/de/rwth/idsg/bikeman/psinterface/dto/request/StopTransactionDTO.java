package de.rwth.idsg.bikeman.psinterface.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

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
    private Long timestamp;
}
