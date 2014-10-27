package de.rwth.idsg.bikeman.ixsi.processor.query;

import de.rwth.idsg.bikeman.ixsi.schema.SessionIDType;
import de.rwth.idsg.bikeman.ixsi.schema.UserTriggeredResponseChoice;
import lombok.Getter;
import lombok.Setter;

import javax.xml.datatype.Duration;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 21.10.2014
 */
@Getter
@Setter
public class UserResponseParams<T extends UserTriggeredResponseChoice> {
    private SessionIDType sessionID;
    private Duration sessionTimeout;
    private T response;
}