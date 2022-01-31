package sn.coundoul.gestion.infrastructure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.infrastructure.web.rest.TestUtil;

class ProjetsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Projets.class);
        Projets projets1 = new Projets();
        projets1.setId(1L);
        Projets projets2 = new Projets();
        projets2.setId(projets1.getId());
        assertThat(projets1).isEqualTo(projets2);
        projets2.setId(2L);
        assertThat(projets1).isNotEqualTo(projets2);
        projets1.setId(null);
        assertThat(projets1).isNotEqualTo(projets2);
    }
}
