package de.rwth.idsg.bikeman.app.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;


@Getter
@Setter
public class CreatePasswordDTO {
    @NotBlank
    @Email
    private String login;

    @Length(min=20, max=20)
    private String key;

    @Pattern(regexp = "^(?=.*[a-z]+.*)(?=.*[0-9A-Z\\p{Punct}]+.*)[0-9a-zA-Z\\p{Punct}]{8,20}$")
    private String password;

    @NotBlank
    private String passwordConfirm;

}
