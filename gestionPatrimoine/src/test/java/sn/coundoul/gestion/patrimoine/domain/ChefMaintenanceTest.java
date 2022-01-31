package sn.coundoul.gestion.patrimoine.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.patrimoine.web.rest.TestUtil;

class ChefMaintenanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChefMaintenance.class);
        ChefMaintenance chefMaintenance1 = new ChefMaintenance();
        chefMaintenance1.setId(1L);
        ChefMaintenance chefMaintenance2 = new ChefMaintenance();
        chefMaintenance2.setId(chefMaintenance1.getId());
        assertThat(chefMaintenance1).isEqualTo(chefMaintenance2);
        chefMaintenance2.setId(2L);
        assertThat(chefMaintenance1).isNotEqualTo(chefMaintenance2);
        chefMaintenance1.setId(null);
        assertThat(chefMaintenance1).isNotEqualTo(chefMaintenance2);
    }
}
