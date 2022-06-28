package sn.coundoul.gestion.equipement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.equipement.web.rest.TestUtil;

class CategorieMatiereTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategorieMatiere.class);
        CategorieMatiere categorieMatiere1 = new CategorieMatiere();
        categorieMatiere1.setId(1L);
        CategorieMatiere categorieMatiere2 = new CategorieMatiere();
        categorieMatiere2.setId(categorieMatiere1.getId());
        assertThat(categorieMatiere1).isEqualTo(categorieMatiere2);
        categorieMatiere2.setId(2L);
        assertThat(categorieMatiere1).isNotEqualTo(categorieMatiere2);
        categorieMatiere1.setId(null);
        assertThat(categorieMatiere1).isNotEqualTo(categorieMatiere2);
    }
}
