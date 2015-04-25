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
    @Pattern(regexp = "^([A-Z0-9]{22})$")
    @JsonProperty("IBAN")
    @JsonView(CreateCustomerDTO.View.class)
    private String IBAN;

    @JsonProperty("BIC")
    @JsonView(CreateCustomerDTO.View.class)
    private String BIC;
}
