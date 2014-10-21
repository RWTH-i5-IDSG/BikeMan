package de.rwth.idsg.bikeman.ixsi.processor.query;

import de.rwth.idsg.bikeman.ixsi.schema.AuthType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.UserTriggeredRequestChoice;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 16.10.2014
 */
public interface UserRequestProcessor {
    UserResponseParams process(Language lan, AuthType auth, UserTriggeredRequestChoice c);
}