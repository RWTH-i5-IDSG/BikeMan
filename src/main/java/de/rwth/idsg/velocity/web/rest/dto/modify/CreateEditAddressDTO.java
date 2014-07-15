package de.rwth.idsg.velocity.web.rest.dto.modify;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @version 15.07.2014
 */
@ToString(includeFieldNames = true)
public class CreateEditAddressDTO {

    @Getter @Setter
    private Long addressId;

    @NotBlank
    @Getter @Setter
    private String streetAndHousenumber;

    @NotBlank
    @Getter @Setter
    private String zip;

    @NotBlank
    @Getter @Setter
    private String city;

    @NotBlank
    @Getter @Setter
    private String country;
}