package de.rwth.idsg.bikeman.web.rest.dto.view;

import de.rwth.idsg.bikeman.domain.OperationState;
import lombok.Data;

/**
 * Created by swam on 16/10/14.
 */
@Data
public class ViewCardAccountDTO {
    final private String cardId;
    final private String cardPin;
    final private Boolean inTransaction;
    final private OperationState operationState;
}
