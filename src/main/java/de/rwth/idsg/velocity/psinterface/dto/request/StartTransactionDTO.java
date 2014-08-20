package de.rwth.idsg.velocity.psinterface.dto.request;

import lombok.Data;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class StartTransactionDTO {
    private Long userId;
    private String pedelecManufacturerId;
    private String stationManufacturerId;
    private String slotManufacturerId;
    private Long timestamp;
}
