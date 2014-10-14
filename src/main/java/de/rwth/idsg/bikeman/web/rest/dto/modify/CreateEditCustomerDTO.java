package de.rwth.idsg.bikeman.web.rest.dto.modify;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

/**
 * Created by swam on 05/06/14.
 */
@Getter
@Setter
@ToString(includeFieldNames = true)
public class CreateEditCustomerDTO {

    private Long userId;

    @NotBlank
    @Email
    private String login;

    @NotBlank
    private String customerId;

    @NotBlank
    private String cardId;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @Valid
    private CreateEditAddressDTO address;

    @Past
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthday;

    private Boolean isActivated;

}
