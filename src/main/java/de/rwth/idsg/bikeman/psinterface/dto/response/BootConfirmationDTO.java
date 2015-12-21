package de.rwth.idsg.bikeman.psinterface.dto.response;

import lombok.Data;

import java.util.List;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class BootConfirmationDTO {
    private Long timestamp;
    private Integer heartbeatInterval;
    private List<CardKeyDTO.ReadOnly> cardKeys;
}
