package de.rwth.idsg.velocity.web.rest.dto.modify;

import de.rwth.idsg.velocity.domain.Address;
import de.rwth.idsg.velocity.domain.OperationState;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;

/**
 * Created by swam on 23/05/14.
 */

@ToString(includeFieldNames = true)
public class CreateEditStationDTO {

    @Getter
    @Setter
    private Long stationId;

    @NotBlank
    @Getter
    @Setter
    private String manufacturerId;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Address address;

    @Getter
    @Setter
    private BigDecimal locationLatitude;

    @Getter
    @Setter
    private BigDecimal locationLongitude;

    @Getter
    @Setter
    private String note;

    @Getter
    @Setter
    private OperationState state;

}
