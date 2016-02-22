package de.rwth.idsg.bikeman.psinterface.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.rwth.idsg.bikeman.psinterface.UnixTimestampDeserializer;
import de.rwth.idsg.bikeman.psinterface.UnixTimestampSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.joda.time.DateTime;

/**
 * Created by Wolfgang Kluth on 06/05/15.
 */

@Data
@AllArgsConstructor
@ToString(includeFieldNames = true)
public class ReserveNowDTO {
    private String pedelecId;
    private String cardId;

    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    @JsonSerialize(using = UnixTimestampSerializer.class)
    private DateTime expiryDate;
}
