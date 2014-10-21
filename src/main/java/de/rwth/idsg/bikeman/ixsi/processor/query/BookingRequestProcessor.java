package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.schema.AuthType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class BookingRequestProcessor implements UserRequestProcessor<BookingRequestType, BookingResponseType> {

    @Override
    public UserResponseParams<BookingResponseType> process(Optional<Language> lan, AuthType auth, BookingRequestType request) {
        return null;
    }
}