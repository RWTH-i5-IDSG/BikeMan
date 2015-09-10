package de.rwth.idsg.bikeman.psinterface;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.09.2015
 */
public class CustomDoubleDeserializer extends JsonDeserializer<Double> {

    @Override
    public Double deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {

        return jsonParser.getDecimalValue()
                         .setScale(20, BigDecimal.ROUND_HALF_UP)
                         .doubleValue();
    }
}
