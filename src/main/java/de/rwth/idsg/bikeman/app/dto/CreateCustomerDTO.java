package de.rwth.idsg.bikeman.app.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateSerializer;
import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames = true)
public class CreateCustomerDTO {
    public interface View {};

    @NotBlank
    @Email
    @JsonView(View.class)
    private String login;

    private String password;

    @NotNull
    @Pattern(regexp = "^([0-9]{4})$")
    private String cardPin;

    @NotBlank
    @JsonView(View.class)
    private String firstname;

    @NotBlank
    @JsonView(View.class)
    private String lastname;

    @Past
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonView(View.class)
    private LocalDate birthday;

    @Valid
    @JsonView(View.class)
    private CreateAddressDTO address;

    @Valid
    @JsonView(View.class)
    private BankAccountDTO bankAccount;
}
