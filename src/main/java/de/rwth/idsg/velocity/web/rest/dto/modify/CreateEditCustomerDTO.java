package de.rwth.idsg.velocity.web.rest.dto.modify;

import de.rwth.idsg.velocity.domain.Address;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

/**
 * Created by swam on 05/06/14.
 */

@ToString(includeFieldNames = true)
public class CreateEditCustomerDTO {

    @Getter
    @Setter
    private Long customerId;

    @Getter
    @Setter
    private String cardId;

    @Getter
    @Setter
    private String firstname;

    @Getter
    @Setter
    private String lastname;

    @Getter
    @Setter
    private Address address;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @Getter
    @Setter
    private LocalDate birthday;

    @Getter
    @Setter
    private String mailAddress;

}
