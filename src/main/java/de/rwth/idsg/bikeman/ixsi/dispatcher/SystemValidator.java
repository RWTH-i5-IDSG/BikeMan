package de.rwth.idsg.bikeman.ixsi.dispatcher;

import de.rwth.idsg.bikeman.ixsi.schema.SystemIDType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 23.10.2014
 */
@Slf4j
@Component
public class SystemValidator {

    public boolean validate(SystemIDType systemIDType) {
        // TODO
        return true;
    }
}
