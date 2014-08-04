package de.rwth.idsg.velocity.psinterface.dto.request;

import lombok.Data;

/**
 * Created by swam on 31/07/14.
 */

@Data
public class CustomerAuthorizeDTO {
    private String cardId;
    private Integer pin;
}
