package de.rwth.idsg.bikeman.psinterface.dto.request;

import lombok.Data;

/**
 * Created by swam on 20/01/15.
 */

@Data
public class CardActivationDTO {
    private String activationKey;
    private String cardPIN;
}
