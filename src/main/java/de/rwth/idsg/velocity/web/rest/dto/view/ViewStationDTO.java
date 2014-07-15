package de.rwth.idsg.velocity.web.rest.dto.view;

import de.rwth.idsg.velocity.domain.OperationState;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by swam on 23/05/14.
 */
@Getter
@ToString(includeFieldNames = true)
public class ViewStationDTO {

    private Long stationId;
    private String manufacturerId;
    private String name;
    private ViewAddressDTO address;
    private BigDecimal locationLatitude;
    private BigDecimal locationLongitude;
    private String note;
    private OperationState state;
    private Long numFreeSlots;
    private Long numAllSlots;
    @Setter private List<ViewStationSlotDTO> slots;

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
}