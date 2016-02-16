package de.rwth.idsg.bikeman.web.rest.dto.modify;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by sgokay on 22.05.14.
 */
@Getter
@Setter
@ToString(includeFieldNames = true)
public class CreateEditManagerDTO {

    private Long userId;

    @NotBlank
    @Email
    private String login;

    @NotBlank
    private String password;

    private String cardId;

    // check if cardPin has only four digits, e.g., '0034'
    @Pattern(regexp = "(^([0-9]{4})$|^$)")
    private String cardPin;
}
