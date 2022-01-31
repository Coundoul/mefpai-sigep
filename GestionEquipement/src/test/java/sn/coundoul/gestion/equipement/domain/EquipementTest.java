package sn.coundoul.gestion.equipement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.equipement.web.rest.TestUtil;

class EquipementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Equipement.class);
        Equipement equipement1 = new Equipement();
        equipement1.setId(1L);
        Equipement equipement2 = new Equipement();
        equipement2.setId(equipement1.getId());
        assertThat(equipement1).isEqualTo(equipement2);
        equipement2.setId(2L);
        assertThat(equipement1).isNotEqualTo(equipement2);
        equipement1.setId(null);
        assertThat(equipement1).isNotEqualTo(equipement2);
    }
}
