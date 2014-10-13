package de.rwth.idsg.bikeman.web.rest.dto.view;

import lombok.Data;

/**
 * Not a standalone object to be sent to frontend, but it is included in other DTOs.
 *
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @version 15.07.2014
 */
@Data
public class ViewAddressDTO {
    private final String streetAndHousenumber;
    private final String zip;
    private final String city;
    private final String country;
}