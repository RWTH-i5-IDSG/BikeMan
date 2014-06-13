package de.rwth.idsg.velocity.web.rest.dto.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by swam on 11/06/14.
 */

@AllArgsConstructor
@ToString(includeFieldNames = true)
public class ViewManagerDTO {

    @Getter private Long userId;
    @Getter private String login;
}
