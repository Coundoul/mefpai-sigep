package sn.coundoul.gestion.infrastructure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.infrastructure.web.rest.TestUtil;

class CorpsEtatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CorpsEtat.class);
        CorpsEtat corpsEtat1 = new CorpsEtat();
        corpsEtat1.setId(1L);
        CorpsEtat corpsEtat2 = new CorpsEtat();
        corpsEtat2.setId(corpsEtat1.getId());
        assertThat(corpsEtat1).isEqualTo(corpsEtat2);
        corpsEtat2.setId(2L);
        assertThat(corpsEtat1).isNotEqualTo(corpsEtat2);
        corpsEtat1.setId(null);
        assertThat(corpsEtat1).isNotEqualTo(corpsEtat2);
    }
}
