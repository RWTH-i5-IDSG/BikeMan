package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.schema.AuthType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.UserTriggeredRequestChoice;
import de.rwth.idsg.bikeman.ixsi.schema.UserTriggeredResponseChoice;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 16.10.2014
 */
public interface UserRequestProcessor<T1 extends UserTriggeredRequestChoice, T2 extends UserTriggeredResponseChoice> {
    UserResponseParams<T2> process(Optional<Language> lan, AuthType auth, T1 request);
}