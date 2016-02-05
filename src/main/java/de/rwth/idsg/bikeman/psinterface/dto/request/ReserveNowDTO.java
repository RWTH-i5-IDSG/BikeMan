package de.rwth.idsg.bikeman.psinterface.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.rwth.idsg.bikeman.psinterface.UnixTimestampDeserializer;
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
    private DateTime expiryDate;
}
