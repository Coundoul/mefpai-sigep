package sn.coundoul.gestion.utilisateurs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.utilisateurs.web.rest.TestUtil;

class OrdonnaceurMatiereTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrdonnaceurMatiere.class);
        OrdonnaceurMatiere ordonnaceurMatiere1 = new OrdonnaceurMatiere();
        ordonnaceurMatiere1.setId(1L);
        OrdonnaceurMatiere ordonnaceurMatiere2 = new OrdonnaceurMatiere();
        ordonnaceurMatiere2.setId(ordonnaceurMatiere1.getId());
        assertThat(ordonnaceurMatiere1).isEqualTo(ordonnaceurMatiere2);
        ordonnaceurMatiere2.setId(2L);
        assertThat(ordonnaceurMatiere1).isNotEqualTo(ordonnaceurMatiere2);
        ordonnaceurMatiere1.setId(null);
        assertThat(ordonnaceurMatiere1).isNotEqualTo(ordonnaceurMatiere2);
    }
}
