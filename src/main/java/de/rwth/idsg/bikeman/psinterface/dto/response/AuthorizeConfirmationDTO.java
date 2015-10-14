package de.rwth.idsg.bikeman.psinterface.dto.response;

import de.rwth.idsg.bikeman.psinterface.dto.AccountState;
import lombok.Data;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class AuthorizeConfirmationDTO {
    private final String cardId;
    private final Integer actualRentedCount;
    private final Integer canRentCount;
}
