package de.rwth.idsg.bikeman.psinterface.dto.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 21.12.2015
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class CardActivationResponseDTO {
    private final String cardId;
    private final String readKey;
    private final String writeKey;
    private final String applicationKey;
}
