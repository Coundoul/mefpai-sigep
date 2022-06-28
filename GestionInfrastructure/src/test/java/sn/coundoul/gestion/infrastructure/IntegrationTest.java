package sn.coundoul.gestion.infrastructure;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import sn.coundoul.gestion.infrastructure.GestionInfrastructureApp;
import sn.coundoul.gestion.infrastructure.config.TestSecurityConfiguration;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { GestionInfrastructureApp.class, TestSecurityConfiguration.class })
public @interface IntegrationTest {
}
