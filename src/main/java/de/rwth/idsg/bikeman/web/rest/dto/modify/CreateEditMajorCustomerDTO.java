package de.rwth.idsg.bikeman.web.rest.dto.modify;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by swam on 16/10/14.
 */

@Data
public class CreateEditMajorCustomerDTO {

    private Long userId;

    @NotBlank
    @Email
    private String login;

    private String password;

    @NotBlank
    private String name;

}
