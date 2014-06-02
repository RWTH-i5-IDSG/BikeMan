package de.rwth.idsg.velocity.web.rest.dto.view;

import de.rwth.idsg.velocity.domain.OperationState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by swam on 23/05/14.
 */

@ToString(includeFieldNames = true)
public class ViewStationDTO {

    @Getter private Long stationId;
    @Getter private String manufacturerId;
    @Getter private String name;
    @Getter private ViewAddressDTO address;
    @Getter private BigDecimal locationLatitude;
    @Getter private BigDecimal locationLongitude;
    @Getter private String note;
    @Getter private OperationState state;
    @Getter private Long numFreeSlots;
    @Getter private Long numAllSlots;
    @Getter @Setter private List<ViewStationSlotDTO> slots;

    public ViewStationDTO(Long stationId, String manufacturerId, String name,
                          String streetAndHousenumber, String zip, String city, String country,
                          BigDecimal locationLatitude, BigDecimal locationLongitude, String note, OperationState state,
                          Long numFreeSlots, Long numAllSlots) {
        this.stationId = stationId;
        this.manufacturerId = manufacturerId;
        this.name = name;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.note = note;
        this.state = state;
        this.numFreeSlots = numFreeSlots;
        this.numAllSlots = numAllSlots;

        this.address = new ViewAddressDTO(streetAndHousenumber, zip, city, country);
    }

    @AllArgsConstructor
    @ToString(includeFieldNames = true)
    class ViewAddressDTO {

        @Getter private String streetAndHousenumber;
        @Getter private String zip;
        @Getter private String city;
        @Getter private String country;
    }
}