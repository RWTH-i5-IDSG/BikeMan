package de.rwth.idsg.velocity.web.rest.dto;

import java.util.List;

public class UserDTO {

    private String login;

    private String email;

    private List<String> roles;

    public UserDTO() {
    }

    public UserDTO(String login, String email, List<String> roles) {
        this.login = login;
        this.email = email;
        this.roles = roles;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserDTO{");
        sb.append("login='").append(login).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", roles=").append(roles);
        sb.append('}');
        return sb.toString();
    }
}
