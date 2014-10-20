package de.rwth.idsg.bikeman.web.rest.dto.view;

import de.rwth.idsg.bikeman.domain.OperationState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Builder;

/**
 * Created by swam on 16/10/14.
 */

@Builder
@Data
@AllArgsConstructor
public class ViewCardAccountDTO {
    private String cardId;
    private String cardPin;
    private Boolean inTransaction;
    private OperationState operationState;
}
