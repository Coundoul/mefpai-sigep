package sn.coundoul.gestion.utilisateurs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.utilisateurs.web.rest.TestUtil;

class IntendantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Intendant.class);
        Intendant intendant1 = new Intendant();
        intendant1.setId(1L);
        Intendant intendant2 = new Intendant();
        intendant2.setId(intendant1.getId());
        assertThat(intendant1).isEqualTo(intendant2);
        intendant2.setId(2L);
        assertThat(intendant1).isNotEqualTo(intendant2);
        intendant1.setId(null);
        assertThat(intendant1).isNotEqualTo(intendant2);
    }
}
