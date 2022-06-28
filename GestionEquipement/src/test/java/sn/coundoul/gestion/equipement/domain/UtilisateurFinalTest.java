package sn.coundoul.gestion.equipement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.equipement.web.rest.TestUtil;

class UtilisateurFinalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UtilisateurFinal.class);
        UtilisateurFinal utilisateurFinal1 = new UtilisateurFinal();
        utilisateurFinal1.setId(1L);
        UtilisateurFinal utilisateurFinal2 = new UtilisateurFinal();
        utilisateurFinal2.setId(utilisateurFinal1.getId());
        assertThat(utilisateurFinal1).isEqualTo(utilisateurFinal2);
        utilisateurFinal2.setId(2L);
        assertThat(utilisateurFinal1).isNotEqualTo(utilisateurFinal2);
        utilisateurFinal1.setId(null);
        assertThat(utilisateurFinal1).isNotEqualTo(utilisateurFinal2);
    }
}
