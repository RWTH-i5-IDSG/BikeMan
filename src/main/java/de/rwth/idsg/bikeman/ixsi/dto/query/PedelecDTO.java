package de.rwth.idsg.bikeman.ixsi.dto.query;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by max on 01/10/14.
 */
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class PedelecDTO {
    private String manufacturerId;
    private Integer maxDistance;

    public PedelecDTO(String manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    // For future maybe
    public PedelecDTO(String manufacturerId, Integer maxDistance) {
        this.manufacturerId = manufacturerId;
        this.maxDistance = maxDistance;
    }
}
