package de.rwth.idsg.velocity.web.rest.dto.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.joda.time.LocalDate;

/**
 * Created by swam on 05/06/14.
 */

@AllArgsConstructor
@ToString(includeFieldNames = true)
public class ViewCustomerDTO {

    @Getter
    private Long customerId;
    @Getter
    private String fullName;
    @Getter
    private String mailAddress;
    @Getter
    private Boolean isActivated;
    @Getter
    private LocalDate birthday;
    @Getter
    private String cardId;

}