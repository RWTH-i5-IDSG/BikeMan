package de.rwth.idsg.bikeman.web.rest.dto.modify;

import lombok.Data;

/**
 * Created by swam on 08/08/14.
 */

@Data
public class PedelecConfigurationDTO {
    private Double maxCurrentValue;
    private Double maxBatteryLevel;
}
