package de.rwth.idsg.bikeman.psinterface;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.time.DateTime;

import java.io.IOException;

/**
 * Problem description:
 *
 * The PS API defines all timestamps as "Unix timestamps". Normal understanding of a "timestamp"
 * in Java is in milliseconds, but Unix timestamps are in seconds!
 *
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 05.02.2016
 */
public class UnixTimestampSerializer extends JsonSerializer<DateTime> {

    @Override
    public void serialize(DateTime value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        // TODO: not sure about null handling
        if (value == null) {
            provider.defaultSerializeNull(jgen);
        } else {
            long millis = value.getMillis();
            long seconds = Utils.toSeconds(millis);
            jgen.writeNumber(seconds);
        }
    }
}
