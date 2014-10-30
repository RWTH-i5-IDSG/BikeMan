package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import de.rwth.idsg.ixsi.jaxb.UserTriggeredRequestChoice;
import de.rwth.idsg.ixsi.jaxb.UserTriggeredResponseChoice;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 16.10.2014
 */
public interface UserRequestProcessor<T1 extends UserTriggeredRequestChoice, T2 extends UserTriggeredResponseChoice> {

    UserResponseParams<T2> processAnonymously(T1 request,
                                              Optional<Language> lan);
    UserResponseParams<T2> processForUser(T1 request,
                                          Optional<Language> lan, UserInfoType userInfo);

    UserResponseParams<T2> invalidSystem();
    UserResponseParams<T2> invalidUserAuth();
}