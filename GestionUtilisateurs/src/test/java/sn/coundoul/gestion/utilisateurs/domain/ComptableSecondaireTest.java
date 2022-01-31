package sn.coundoul.gestion.utilisateurs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.utilisateurs.web.rest.TestUtil;

class ComptableSecondaireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComptableSecondaire.class);
        ComptableSecondaire comptableSecondaire1 = new ComptableSecondaire();
        comptableSecondaire1.setId(1L);
        ComptableSecondaire comptableSecondaire2 = new ComptableSecondaire();
        comptableSecondaire2.setId(comptableSecondaire1.getId());
        assertThat(comptableSecondaire1).isEqualTo(comptableSecondaire2);
        comptableSecondaire2.setId(2L);
        assertThat(comptableSecondaire1).isNotEqualTo(comptableSecondaire2);
        comptableSecondaire1.setId(null);
        assertThat(comptableSecondaire1).isNotEqualTo(comptableSecondaire2);
    }
}
