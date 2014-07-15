package de.rwth.idsg.velocity.web.rest.dto.view;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.rwth.idsg.velocity.web.rest.dto.util.CustomLocalDateSerializer;
import lombok.Getter;
import lombok.ToString;
import org.joda.time.LocalDate;

/**
 * Created by swam on 05/06/14.
 */
@ToString(includeFieldNames = true)
public class ViewCustomerDTO {

    @Getter private Long userId;
    @Getter private String login;
    @Getter private String customerId;
    @Getter private String firstname;
    @Getter private String lastname;
    @Getter private Boolean isActivated;
    @Getter private String cardId;
    @Getter private ViewAddressDTO address;

    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @Getter private LocalDate birthday;

    public ViewCustomerDTO(Long userId, String login, String customerId, String firstname, String lastname,
                           Boolean isActivated, LocalDate birthday, String cardId,
                           String streetAndHousenumber, String zip, String city, String country) {
        this.userId = userId;
        this.login = login;
        this.customerId = customerId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.isActivated = isActivated;
        this.birthday = birthday;
        this.cardId = cardId;

        if (streetAndHousenumber != null) {
            this.address = new ViewAddressDTO(streetAndHousenumber, zip, city, country);
        }
    }
}