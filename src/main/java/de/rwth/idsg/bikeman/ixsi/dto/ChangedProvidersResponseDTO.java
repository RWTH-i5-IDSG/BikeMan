package de.rwth.idsg.bikeman.ixsi.dto;

import lombok.Data;

/**
 * Created by max on 06/10/14.
 */
@Data
public class ChangedProvidersResponseDTO {
    private long timestamp;
    private boolean providersChanged;
}
