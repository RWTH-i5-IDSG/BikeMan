package de.rwth.idsg.bikeman.app.dto;

import lombok.Data;

@Data
public class ViewAddressDTO {
    private final String streetAndHousenumber;
    private final String zip;
    private final String city;
    private final String country;
}