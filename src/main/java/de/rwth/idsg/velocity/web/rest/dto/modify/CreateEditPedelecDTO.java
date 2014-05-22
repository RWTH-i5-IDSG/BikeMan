package de.rwth.idsg.velocity.web.rest.dto.modify;

import de.rwth.idsg.velocity.domain.OperationState;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by sgokay on 22.05.14.
 */
@ToString(includeFieldNames = true)
public class CreateEditPedelecDTO {

    /**
     * Create: Mandatory user input
     * Edit: Ignore
     */
    @NotBlank
    @Getter @Setter
    private String manufacturerId;

    /**
     * Create: Ignore
     * Edit: Mandatory frontend parameter for lookup
     */
    @NotNull
    @Getter @Setter
    private Long pedelecId;

    /**
     * Create: Possible parameter
     * Edit: Mandatory parameter
     */
    @NotNull
    @Getter @Setter
    private OperationState state;

}
