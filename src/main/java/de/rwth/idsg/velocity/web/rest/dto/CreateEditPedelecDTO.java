package de.rwth.idsg.velocity.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import de.rwth.idsg.velocity.domain.PedelecState;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by max on 08/05/14.
 */
public class CreateEditPedelecDTO {

    @NotBlank
    @Getter @Setter private String pedelecId;

    @NotNull
    @Getter @Setter private PedelecState state;

    @JsonCreator
    public CreateEditPedelecDTO() { }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("pedelecId", pedelecId)
                .append("state", state)
                .toString();
    }
}
