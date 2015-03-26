package de.rwth.idsg.bikeman.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    private AuthoritiesConstants() {
    }

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String CUSTOMER = "ROLE_CUSTOMER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String MANAGER = "ROLE_MANAGER";

    public static final String MAJOR_CUSTOMER = "ROLE_MAJOR_CUSTOMER";
}
