package de.rwth.idsg.bikeman.psinterface.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * Created by Wolfgang Kluth on 06/05/15.
 */

@Data
@AllArgsConstructor
@ToString(includeFieldNames = true)
public class CancelReservationDTO {
    String pedelecId;
}
