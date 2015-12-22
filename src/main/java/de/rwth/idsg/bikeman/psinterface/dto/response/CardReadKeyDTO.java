package de.rwth.idsg.bikeman.psinterface.dto.response;

import lombok.Data;
import lombok.Getter;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 28.07.2015
 */
@Getter
@Data
public class CardReadKeyDTO {
    private final String name, readKey;
}
