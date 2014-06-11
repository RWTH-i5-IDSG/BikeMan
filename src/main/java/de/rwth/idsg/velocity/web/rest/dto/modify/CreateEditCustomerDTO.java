package de.rwth.idsg.velocity.web.rest.dto.modify;

import de.rwth.idsg.velocity.domain.Address;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.LocalDate;

import javax.validation.constraints.NotNull;

/**
 * Created by swam on 05/06/14.
 */
@ToString(includeFieldNames = true)
public class CreateEditCustomerDTO {

    @NotBlank
    @Getter @Setter
    private String login;

    @NotBlank
    @Getter @Setter
    private String customerId;

    @NotBlank
    @Getter @Setter
    private String cardId;

    @NotBlank
    @Getter @Setter
    private String firstname;

    @NotBlank
    @Getter @Setter
    private String lastname;

    @NotNull
    @Getter @Setter
    private Address address;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @Getter @Setter
    private LocalDate birthday;

    @NotNull
    @Getter @Setter
    private Boolean isActivated;

}
