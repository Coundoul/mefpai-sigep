package sn.coundoul.gestion.patrimoine.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.patrimoine.web.rest.TestUtil;

class ComptablePrincipaleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComptablePrincipale.class);
        ComptablePrincipale comptablePrincipale1 = new ComptablePrincipale();
        comptablePrincipale1.setId(1L);
        ComptablePrincipale comptablePrincipale2 = new ComptablePrincipale();
        comptablePrincipale2.setId(comptablePrincipale1.getId());
        assertThat(comptablePrincipale1).isEqualTo(comptablePrincipale2);
        comptablePrincipale2.setId(2L);
        assertThat(comptablePrincipale1).isNotEqualTo(comptablePrincipale2);
        comptablePrincipale1.setId(null);
        assertThat(comptablePrincipale1).isNotEqualTo(comptablePrincipale2);
    }
}
