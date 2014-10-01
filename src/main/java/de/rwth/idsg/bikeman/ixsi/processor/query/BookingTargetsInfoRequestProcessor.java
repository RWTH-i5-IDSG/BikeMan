package de.rwth.idsg.bikeman.ixsi.processor.query;

import de.rwth.idsg.bikeman.ixsi.processor.Processor;
import de.rwth.idsg.bikeman.ixsi.schema.BookingTargetsInfoRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingTargetsInfoResponseType;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
public class BookingTargetsInfoRequestProcessor implements
        Processor<BookingTargetsInfoRequestType, BookingTargetsInfoResponseType> {

    @Override
    public BookingTargetsInfoResponseType process(BookingTargetsInfoRequestType request) {
        return null;
    }
}