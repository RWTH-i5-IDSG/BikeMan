package de.rwth.idsg.bikeman.web.rest.dto.monitor;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by max on 15/07/15.
 */
@Getter
@Setter
public class EndpointDTO {
    private String systemId;
    private String sessionId;
    private boolean isOpen;
}
