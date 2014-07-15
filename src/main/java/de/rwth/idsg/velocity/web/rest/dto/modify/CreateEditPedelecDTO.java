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
@Getter
@Setter
@ToString(includeFieldNames = true)
public class CreateEditPedelecDTO {

    private Long pedelecId;

    @NotBlank
    private String manufacturerId;

    @NotNull
    private OperationState state;
}
