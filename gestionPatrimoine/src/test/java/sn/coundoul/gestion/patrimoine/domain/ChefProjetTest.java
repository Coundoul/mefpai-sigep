package sn.coundoul.gestion.patrimoine.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.patrimoine.web.rest.TestUtil;

class ChefProjetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChefProjet.class);
        ChefProjet chefProjet1 = new ChefProjet();
        chefProjet1.setId(1L);
        ChefProjet chefProjet2 = new ChefProjet();
        chefProjet2.setId(chefProjet1.getId());
        assertThat(chefProjet1).isEqualTo(chefProjet2);
        chefProjet2.setId(2L);
        assertThat(chefProjet1).isNotEqualTo(chefProjet2);
        chefProjet1.setId(null);
        assertThat(chefProjet1).isNotEqualTo(chefProjet2);
    }
}
