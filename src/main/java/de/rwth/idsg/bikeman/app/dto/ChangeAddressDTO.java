package de.rwth.idsg.bikeman.app.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@ToString(includeFieldNames = true)
public class ChangeAddressDTO {

    @NotBlank
    private String streetAndHousenumber;

    @NotBlank
    private String zip;

    @NotBlank
    private String city;

    @NotBlank
    private String country;

}
