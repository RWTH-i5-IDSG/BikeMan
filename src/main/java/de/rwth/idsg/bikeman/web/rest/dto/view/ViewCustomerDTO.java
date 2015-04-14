package de.rwth.idsg.bikeman.web.rest.dto.view;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.rwth.idsg.bikeman.domain.TariffType;
import de.rwth.idsg.bikeman.web.rest.dto.util.CustomLocalDateSerializer;
import lombok.Getter;
import lombok.ToString;
import org.joda.time.LocalDate;

/**
 * Created by swam on 05/06/14.
 */
@Getter
@ToString(includeFieldNames = true)
public class ViewCustomerDTO {

    private Long userId;
    private String login;
    private String customerId;
    private String firstname;
    private String lastname;
    private Boolean isActivated;
    private String cardId;
    private String cardPin;
    private TariffType tariff;
    private ViewAddressDTO address;

    @JsonSerialize(using = CustomLocalDateSerializer.class)
    private LocalDate birthday;

    public ViewCustomerDTO(Long userId, String login, String customerId, String firstname, String lastname,
                           Boolean isActivated, LocalDate birthday, String cardId, String cardPin, TariffType tariff,
                           String streetAndHousenumber, String zip, String city, String country) {
        this.userId = userId;
        this.login = login;
        this.customerId = customerId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.isActivated = isActivated;
        this.birthday = birthday;
        this.cardId = cardId;
        this.cardPin = cardPin;
        this.tariff = tariff;

        if (streetAndHousenumber != null) {
            this.address = new ViewAddressDTO(streetAndHousenumber, zip, city, country);
        }
    }
}
