package sn.coundoul.gestion.equipement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.equipement.web.rest.TestUtil;

class AffectationsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Affectations.class);
        Affectations affectations1 = new Affectations();
        affectations1.setId(1L);
        Affectations affectations2 = new Affectations();
        affectations2.setId(affectations1.getId());
        assertThat(affectations1).isEqualTo(affectations2);
        affectations2.setId(2L);
        assertThat(affectations1).isNotEqualTo(affectations2);
        affectations1.setId(null);
        assertThat(affectations1).isNotEqualTo(affectations2);
    }
}
