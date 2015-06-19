package de.rwth.idsg.bikeman.psinterface.dto.response;

import lombok.Data;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class BootConfirmationDTO {
    private Long timestamp;
    private Integer heartbeatInterval;
    private String cardReadEncrKey;
    private String cardWriteEncrKey;
}
