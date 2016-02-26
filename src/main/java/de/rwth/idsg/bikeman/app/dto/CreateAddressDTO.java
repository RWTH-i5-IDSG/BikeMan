package de.rwth.idsg.bikeman.app.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@ToString(includeFieldNames = true)
public class CreateAddressDTO {

    @NotBlank
    @JsonView(CreateCustomerDTO.View.class)
    private String streetAndHousenumber;

    @NotBlank
    @JsonView(CreateCustomerDTO.View.class)
    private String zip;

    @NotBlank
    @JsonView(CreateCustomerDTO.View.class)
    private String city;

    @NotBlank
    @JsonView(CreateCustomerDTO.View.class)
    private String country;
}
