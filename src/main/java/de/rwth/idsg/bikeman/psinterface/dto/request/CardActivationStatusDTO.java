package de.rwth.idsg.bikeman.psinterface.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 21.12.2015
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardActivationStatusDTO {
    private String cardId;
    private boolean successfulActivation;
}
