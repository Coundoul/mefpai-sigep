package sn.coundoul.gestion.patrimoine.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.patrimoine.web.rest.TestUtil;

class DetenteurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Detenteur.class);
        Detenteur detenteur1 = new Detenteur();
        detenteur1.setId(1L);
        Detenteur detenteur2 = new Detenteur();
        detenteur2.setId(detenteur1.getId());
        assertThat(detenteur1).isEqualTo(detenteur2);
        detenteur2.setId(2L);
        assertThat(detenteur1).isNotEqualTo(detenteur2);
        detenteur1.setId(null);
        assertThat(detenteur1).isNotEqualTo(detenteur2);
    }
}
