package de.rwth.idsg.bikeman.web.rest.dto.view;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

/**
 * Created by swam on 16/10/14.
 */

@Data
public class ViewMajorCustomerDTO {
    private Long userId;
    private String login;
    private String name;

    private Set<ViewCardAccountDTO> cardAccountDTOs;

    public ViewMajorCustomerDTO(Long userId, String login, String name) {
        this.userId = userId;
        this.login = login;
        this.name = name;
    }
}
