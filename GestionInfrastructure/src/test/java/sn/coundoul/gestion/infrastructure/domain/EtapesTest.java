package sn.coundoul.gestion.infrastructure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.infrastructure.web.rest.TestUtil;

class EtapesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Etapes.class);
        Etapes etapes1 = new Etapes();
        etapes1.setId(1L);
        Etapes etapes2 = new Etapes();
        etapes2.setId(etapes1.getId());
        assertThat(etapes1).isEqualTo(etapes2);
        etapes2.setId(2L);
        assertThat(etapes1).isNotEqualTo(etapes2);
        etapes1.setId(null);
        assertThat(etapes1).isNotEqualTo(etapes2);
    }
}
