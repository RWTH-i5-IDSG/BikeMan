package de.rwth.idsg.bikeman.web.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString(includeFieldNames = true)
public class UserDTO {

    @Getter private String login;
    @Getter private List<String> roles;

    public UserDTO() { }

}
