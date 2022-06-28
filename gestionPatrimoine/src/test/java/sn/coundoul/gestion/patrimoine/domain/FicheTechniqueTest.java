package sn.coundoul.gestion.patrimoine.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.patrimoine.web.rest.TestUtil;

class FicheTechniqueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FicheTechnique.class);
        FicheTechnique ficheTechnique1 = new FicheTechnique();
        ficheTechnique1.setId(1L);
        FicheTechnique ficheTechnique2 = new FicheTechnique();
        ficheTechnique2.setId(ficheTechnique1.getId());
        assertThat(ficheTechnique1).isEqualTo(ficheTechnique2);
        ficheTechnique2.setId(2L);
        assertThat(ficheTechnique1).isNotEqualTo(ficheTechnique2);
        ficheTechnique1.setId(null);
        assertThat(ficheTechnique1).isNotEqualTo(ficheTechnique2);
    }
}
