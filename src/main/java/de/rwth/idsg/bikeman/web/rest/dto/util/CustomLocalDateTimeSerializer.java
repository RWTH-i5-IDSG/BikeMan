package de.rwth.idsg.bikeman.web.rest.dto.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

/**
 * Created by swam on 04/05/14.
 */
public class CustomLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    private static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z");

    @Override
    public void serialize(LocalDateTime value, JsonGenerator generator,
                          SerializerProvider serializerProvider)
            throws IOException {

        generator.writeString(formatter.print(value));
    }
}
