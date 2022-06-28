package sn.coundoul.gestion.patrimoine.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.patrimoine.web.rest.TestUtil;

class FicheTechniqueMaintenanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FicheTechniqueMaintenance.class);
        FicheTechniqueMaintenance ficheTechniqueMaintenance1 = new FicheTechniqueMaintenance();
        ficheTechniqueMaintenance1.setId(1L);
        FicheTechniqueMaintenance ficheTechniqueMaintenance2 = new FicheTechniqueMaintenance();
        ficheTechniqueMaintenance2.setId(ficheTechniqueMaintenance1.getId());
        assertThat(ficheTechniqueMaintenance1).isEqualTo(ficheTechniqueMaintenance2);
        ficheTechniqueMaintenance2.setId(2L);
        assertThat(ficheTechniqueMaintenance1).isNotEqualTo(ficheTechniqueMaintenance2);
        ficheTechniqueMaintenance1.setId(null);
        assertThat(ficheTechniqueMaintenance1).isNotEqualTo(ficheTechniqueMaintenance2);
    }
}
