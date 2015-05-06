package de.rwth.idsg.bikeman.psinterface.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by Wolfgang Kluth on 06/05/15.
 */

@Getter
@Builder
@AllArgsConstructor
@ToString(includeFieldNames = true)
public class CancelReservationDTO {
    Long reservationId;
}
