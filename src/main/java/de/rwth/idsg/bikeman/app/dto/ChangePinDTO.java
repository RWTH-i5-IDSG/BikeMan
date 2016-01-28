package de.rwth.idsg.bikeman.app.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

@Setter
@Getter
@ToString(includeFieldNames = true)
public class ChangePinDTO {
    @Pattern(regexp = "^([0-9]{4})$")
    private String cardPin;

    @NotBlank
    private String password;
}
