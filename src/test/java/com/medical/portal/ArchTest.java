package com.medical.portal;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    @Disabled
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.medical.portal");

        noClasses()
            .that()
            .resideInAnyPackage("com.medical.portal.service..")
            .or()
            .resideInAnyPackage("com.medical.portal.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.medical.portal.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
