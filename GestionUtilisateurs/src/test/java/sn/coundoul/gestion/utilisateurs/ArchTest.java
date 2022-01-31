package sn.coundoul.gestion.utilisateurs;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("sn.coundoul.gestion.utilisateurs");

        noClasses()
            .that()
            .resideInAnyPackage("sn.coundoul.gestion.utilisateurs.service..")
            .or()
            .resideInAnyPackage("sn.coundoul.gestion.utilisateurs.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..sn.coundoul.gestion.utilisateurs.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
