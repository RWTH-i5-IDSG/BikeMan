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

    @Getter private String login;
    @Getter private String customerId;
    @Getter private String fullName;
    @Getter private Boolean isActivated;
    @Getter private String cardId;

    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @Getter
    private LocalDate birthday;

    public ViewCustomerDTO(String login, String customerId, String firstname, String lastname,
                           Boolean isActivated, LocalDate birthday, String cardId) {
        this.login = login;
        this.customerId = customerId;
        this.fullName = firstname + " " + lastname;
        this.isActivated = isActivated;
        this.birthday = birthday;
        this.cardId = cardId;
    }

}