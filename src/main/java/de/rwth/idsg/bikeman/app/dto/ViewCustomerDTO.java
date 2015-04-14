package de.rwth.idsg.bikeman.app.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.rwth.idsg.bikeman.domain.TariffType;
import de.rwth.idsg.bikeman.domain.util.CustomLocalDateSerializer;
import lombok.Getter;
import lombok.ToString;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

@Getter
@ToString(includeFieldNames = true)
public class ViewCustomerDTO {

    private String login;
    private String customerId;
    private String firstname;
    private String lastname;
    private Boolean isActivated;
    private ViewAddressDTO address;

    @JsonSerialize(using = CustomLocalDateSerializer.class)
    private LocalDate birthday;

    public ViewCustomerDTO(String customerId, String login, String firstname, String lastname,
                           Boolean isActivated, LocalDate birthday,
                           String streetAndHousenumber, String zip, String city, String country) {

        this.customerId = customerId;
        this.login = login;
        this.firstname = firstname;
        this.lastname = lastname;
        this.isActivated = isActivated;
        this.birthday = birthday;

        if (streetAndHousenumber != null) {
            this.address = new ViewAddressDTO(streetAndHousenumber, zip, city, country);
        }
    }
}
