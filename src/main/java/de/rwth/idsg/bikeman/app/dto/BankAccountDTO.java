package de.rwth.idsg.bikeman.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString(includeFieldNames = true)
public class BankAccountDTO {
    @NotBlank
    @Pattern(regexp = "^([A-Z]{2})([0-9]{2})([a-zA-Z0-9]{1,30})$")
    @JsonProperty("IBAN")
    @JsonView(CreateCustomerDTO.View.class)
    private String IBAN;

    @Pattern(regexp = "^([A-Z]{6})([A-Z0-9]{2})([a-zA-Z0-9]{0,3})$")
    @JsonProperty("BIC")
    @JsonView(CreateCustomerDTO.View.class)
    private String BIC;
}
