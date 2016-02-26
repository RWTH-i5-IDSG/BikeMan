package de.rwth.idsg.bikeman.psinterface.exception;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.rwth.idsg.bikeman.psinterface.UnixTimestampDeserializer;
import de.rwth.idsg.bikeman.psinterface.UnixTimestampSerializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 23.02.2015
 */
@Getter
@Setter
@RequiredArgsConstructor
public class PsExceptionMessage {

    @JsonSerialize(using = UnixTimestampSerializer.class)
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    private final DateTime timestamp;

    private final PsErrorCode code;
    private final String message;
}
