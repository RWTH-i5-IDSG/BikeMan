package de.rwth.idsg.bikeman.ixsi.dto.query;

import de.rwth.idsg.bikeman.ixsi.schema.ProviderIDType;
import lombok.Data;

import java.util.List;

/**
 * Created by max on 06/10/14.
 */
@Data
public class ChangedProvidersResponseDTO {
    private long timestamp;
    private boolean providersChanged;
}
