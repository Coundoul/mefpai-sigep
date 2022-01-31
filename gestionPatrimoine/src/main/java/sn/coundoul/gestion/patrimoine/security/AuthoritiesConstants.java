package sn.coundoul.gestion.patrimoine.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String COMPTABLEPRINCIPALE = "ROLE_COMPTABLE_PRINCIPALE";

    public static final String CHEFPROJET = "ROLE_CHEF_PROJET";

    public static final String CHEFMAINTENANCE = "ROLE_CHEF_MAINTENANCE";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {}
}
