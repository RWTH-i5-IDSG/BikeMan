package de.rwth.idsg.bikeman.ixsi.dto.query;

import de.rwth.idsg.bikeman.ixsi.schema.NonNegativeInteger;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by max on 01/10/14.
 */
@AllArgsConstructor
@Getter
public class PedelecDTO {
    private long pedelecId;
    private String manufacturerId;
    private String placeGroupId;
    private NonNegativeInteger maxDistance;
}
