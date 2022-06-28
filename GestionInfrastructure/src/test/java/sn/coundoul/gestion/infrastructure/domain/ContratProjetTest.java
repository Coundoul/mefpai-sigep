package sn.coundoul.gestion.infrastructure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.infrastructure.web.rest.TestUtil;

class ContratProjetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContratProjet.class);
        ContratProjet contratProjet1 = new ContratProjet();
        contratProjet1.setId(1L);
        ContratProjet contratProjet2 = new ContratProjet();
        contratProjet2.setId(contratProjet1.getId());
        assertThat(contratProjet1).isEqualTo(contratProjet2);
        contratProjet2.setId(2L);
        assertThat(contratProjet1).isNotEqualTo(contratProjet2);
        contratProjet1.setId(null);
        assertThat(contratProjet1).isNotEqualTo(contratProjet2);
    }
}
