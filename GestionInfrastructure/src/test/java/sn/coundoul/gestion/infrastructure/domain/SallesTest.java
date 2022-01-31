package sn.coundoul.gestion.infrastructure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.infrastructure.web.rest.TestUtil;

class SallesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Salles.class);
        Salles salles1 = new Salles();
        salles1.setId(1L);
        Salles salles2 = new Salles();
        salles2.setId(salles1.getId());
        assertThat(salles1).isEqualTo(salles2);
        salles2.setId(2L);
        assertThat(salles1).isNotEqualTo(salles2);
        salles1.setId(null);
        assertThat(salles1).isNotEqualTo(salles2);
    }
}
