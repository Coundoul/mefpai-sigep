package sn.coundoul.gestion.equipement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import sn.coundoul.gestion.equipement.GestionEquipementApp;
import sn.coundoul.gestion.equipement.config.TestSecurityConfiguration;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { GestionEquipementApp.class, TestSecurityConfiguration.class })
public @interface IntegrationTest {
}
