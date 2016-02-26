package de.rwth.idsg.bikeman.app.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

@Getter
public class ChangePasswordDTO {

    @NotBlank
    private String oldPassword;

    @Pattern(regexp = "^(?=.*[a-z]+.*)(?=.*[0-9A-Z\\p{Punct}]+.*)[0-9a-zA-Z\\p{Punct}]{8,20}$")
    private String password;

    @NotBlank
    private String passwordConfirm;
}
