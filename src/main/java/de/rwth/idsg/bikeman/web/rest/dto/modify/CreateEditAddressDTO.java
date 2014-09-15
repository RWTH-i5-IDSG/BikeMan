package de.rwth.idsg.bikeman.web.rest.dto.modify;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @version 15.07.2014
 */
@Getter
@Setter
@ToString(includeFieldNames = true)
public class CreateEditAddressDTO {

    private Long addressId;

    @NotBlank
    private String streetAndHousenumber;

    @NotBlank
    private String zip;

    @NotBlank
    private String city;

    @NotBlank
    private String country;
}