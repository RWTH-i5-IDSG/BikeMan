package de.rwth.idsg.velocity.web.rest.dto.modify;

import de.rwth.idsg.velocity.domain.OperationState;
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
@ToString(includeFieldNames = true)
public class CreateEditStationDTO {

    @Getter @Setter
    private Long stationId;

    @NotBlank
    @Getter @Setter
    private String manufacturerId;

    @Getter @Setter
    private String name;

    @Valid
    @Getter @Setter
    private CreateEditAddressDTO address;

    @Range(min = -90, max = 90)
    @Getter @Setter
    private BigDecimal locationLatitude;

    @Range(min = -180, max = 180)
    @Getter @Setter
    private BigDecimal locationLongitude;

    @Getter @Setter
    private String note;

    @Getter @Setter
    private OperationState state;

}
