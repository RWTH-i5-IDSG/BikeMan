package de.rwth.idsg.velocity.domain.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;

/**
 * Created by swam on 04/05/14.
 */
public class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private final DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTimeParser();


    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        String dateTimeString = jsonParser.getText();

        if (dateTimeString.isEmpty()) {
            return null;
        }

        return dateTimeFormatter.parseLocalDateTime(dateTimeString);
    }
}



//public class CustomLocalDateSerializer extends JsonSerializer<LocalDate> {
//
//    private static DateTimeFormatter formatter =
//            DateTimeFormat.forPattern("yyyy-MM-dd");
//
//    @Override
//    public void serialize(LocalDate value, JsonGenerator generator,
//                          SerializerProvider serializerProvider)
//            throws IOException {
//
//        generator.writeString(formatter.print(value));
//    }
//}
