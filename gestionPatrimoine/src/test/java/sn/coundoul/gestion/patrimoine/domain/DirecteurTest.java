package sn.coundoul.gestion.patrimoine.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.patrimoine.web.rest.TestUtil;

class DirecteurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Directeur.class);
        Directeur directeur1 = new Directeur();
        directeur1.setId(1L);
        Directeur directeur2 = new Directeur();
        directeur2.setId(directeur1.getId());
        assertThat(directeur1).isEqualTo(directeur2);
        directeur2.setId(2L);
        assertThat(directeur1).isNotEqualTo(directeur2);
        directeur1.setId(null);
        assertThat(directeur1).isNotEqualTo(directeur2);
    }
}
