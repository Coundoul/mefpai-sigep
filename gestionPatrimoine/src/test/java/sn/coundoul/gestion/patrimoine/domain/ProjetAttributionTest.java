package sn.coundoul.gestion.patrimoine.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.patrimoine.web.rest.TestUtil;

class ProjetAttributionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjetAttribution.class);
        ProjetAttribution projetAttribution1 = new ProjetAttribution();
        projetAttribution1.setId(1L);
        ProjetAttribution projetAttribution2 = new ProjetAttribution();
        projetAttribution2.setId(projetAttribution1.getId());
        assertThat(projetAttribution1).isEqualTo(projetAttribution2);
        projetAttribution2.setId(2L);
        assertThat(projetAttribution1).isNotEqualTo(projetAttribution2);
        projetAttribution1.setId(null);
        assertThat(projetAttribution1).isNotEqualTo(projetAttribution2);
    }
}
