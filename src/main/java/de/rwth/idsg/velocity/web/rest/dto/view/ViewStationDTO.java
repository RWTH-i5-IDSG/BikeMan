package de.rwth.idsg.velocity.web.rest.dto.view;

import de.rwth.idsg.velocity.domain.Address;
import de.rwth.idsg.velocity.domain.OperationState;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Created by swam on 23/05/14.
 */

@ToString(includeFieldNames = true)
public class ViewStationDTO {

    @Getter
    private Long stationId;
    @Getter
    private String manufacturerId;
    @Getter
    private String name;
    @Getter
    private Address address;
    @Getter
    private BigDecimal locationLatitude;
    @Getter
    private BigDecimal locationLongitude;
    @Getter
    private String note;
    @Getter
    private OperationState state;
    @Getter
    private String allocation;

    public ViewStationDTO(Long stationId, String manufacturerId, String name, Address address, BigDecimal locationLatitude, BigDecimal locationLongitude, String note, OperationState state, String allocation) {
        this.stationId = stationId;
        this.manufacturerId = manufacturerId;
        this.name = name;
        this.address = address;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.note = note;
        this.state = state;
        this.allocation = allocation;
    }
}
