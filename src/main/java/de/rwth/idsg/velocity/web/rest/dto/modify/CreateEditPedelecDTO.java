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

    @Getter @Setter
    private Long pedelecId;

    @NotBlank
    @Getter @Setter
    private String manufacturerId;

    @NotNull
    @Getter @Setter
    private OperationState state;
}
