package sn.coundoul.gestion.utilisateurs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import sn.coundoul.gestion.utilisateurs.GestionUtilisateursApp;
import sn.coundoul.gestion.utilisateurs.config.TestSecurityConfiguration;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { GestionUtilisateursApp.class, TestSecurityConfiguration.class })
public @interface IntegrationTest {
}
