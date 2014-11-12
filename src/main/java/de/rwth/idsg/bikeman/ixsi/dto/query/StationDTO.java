package de.rwth.idsg.bikeman.ixsi.dto.query;

import de.rwth.idsg.bikeman.web.rest.dto.view.ViewAddressDTO;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Created by max on 01/10/14.
 */
@Getter
@ToString(includeFieldNames = true)
public class StationDTO {

    private String manufacturerId;
    private BigDecimal location_longitude;
    private BigDecimal location_latitude;
    private Integer slotCount;
    private String name;
    private String note;
    private ViewAddressDTO address;

    public StationDTO(String manufacturerId, BigDecimal location_longitude, BigDecimal location_latitude,
                      Integer slotCount, String name, String note,
                      String streetAndHousenumber, String zip, String city, String country) {
        this.manufacturerId = manufacturerId;
        this.location_longitude = location_longitude;
        this.location_latitude = location_latitude;
        this.slotCount = slotCount;
        this.name = name;
        this.note = note;
        this.address = new ViewAddressDTO(streetAndHousenumber, zip, city, country);
    }
}