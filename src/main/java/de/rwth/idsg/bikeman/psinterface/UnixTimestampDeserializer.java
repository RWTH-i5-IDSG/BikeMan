package de.rwth.idsg.bikeman.psinterface;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
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
public class UnixTimestampDeserializer extends JsonDeserializer<DateTime> {

    @Override
    public DateTime deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        // TODO: not sure about null handling
        if (JsonToken.VALUE_NUMBER_INT.equals(jp.getCurrentToken())) {
            long seconds = jp.getLongValue();
            long millis = Utils.toMillis(seconds);
            return new DateTime(millis);
        } else {
            return null;
        }
    }
}
