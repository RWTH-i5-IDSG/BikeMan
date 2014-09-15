package de.rwth.idsg.bikeman.web.rest.dto.modify;

import de.rwth.idsg.bikeman.domain.OperationState;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * Created by swam on 23/05/14.
 */
@Getter
@Setter
@ToString(includeFieldNames = true)
public class CreateEditStationDTO {

    private Long stationId;

    @NotBlank
    private String manufacturerId;

    private String name;

    @Valid
    private CreateEditAddressDTO address;

    @Range(min = -90, max = 90)
    private BigDecimal locationLatitude;

    @Range(min = -180, max = 180)
    private BigDecimal locationLongitude;

    private String note;

    private OperationState state;

}
