package de.rwth.idsg.velocity.web.rest.dto.modify;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by sgokay on 22.05.14.
 */
@ToString(includeFieldNames = true)
public class CreateEditManagerDTO {

    @Getter
    @Setter
    private Long userId;

    @NotBlank
    @Getter
    @Setter
    private String login;

    @NotBlank
    @Getter
    @Setter
    private String password;
}
