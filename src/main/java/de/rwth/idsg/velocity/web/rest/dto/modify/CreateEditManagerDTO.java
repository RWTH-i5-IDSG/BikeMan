package de.rwth.idsg.velocity.web.rest.dto.modify;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by sgokay on 22.05.14.
 */
@Getter
@Setter
@ToString(includeFieldNames = true)
public class CreateEditManagerDTO {

    private Long userId;

    @NotBlank
    private String login;

    @NotBlank
    private String password;
}
