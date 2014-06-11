package de.rwth.idsg.velocity.web.rest.dto.view;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.rwth.idsg.velocity.domain.Address;
import de.rwth.idsg.velocity.web.rest.dto.util.CustomLocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.joda.time.LocalDate;

/**
 * Created by swam on 05/06/14.
 */
@ToString(includeFieldNames = true)
public class ViewCustomerDTO {

    @Getter private String login;
    @Getter private String customerId;
    @Getter private String fullName;
    @Getter private Boolean isActivated;
    @Getter private String cardId;
    @Getter private ViewAddressDTO addressDTO;


    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @Getter
    private LocalDate birthday;

    public ViewCustomerDTO(String login, String customerId, String firstname, String lastname,
<<<<<<< HEAD
                           String mailAddress, Boolean isActivated,
                           LocalDate birthday, String cardId, String streetAndHousenumber, String zip, String city, String country) {
=======
                           Boolean isActivated, LocalDate birthday, String cardId) {
>>>>>>> b4e8b8fefdc23a0ad61c907e404b71140d120e35
        this.login = login;
        this.customerId = customerId;
        this.fullName = firstname + " " + lastname;
        this.isActivated = isActivated;
        this.birthday = birthday;
        this.cardId = cardId;
        this.addressDTO = new ViewAddressDTO(streetAndHousenumber, zip, city, country);
    }

    @AllArgsConstructor
    @ToString(includeFieldNames = true)
    class ViewAddressDTO {

        @Getter private String streetAndHousenumber;
        @Getter private String zip;
        @Getter private String city;
        @Getter private String country;
    }

}