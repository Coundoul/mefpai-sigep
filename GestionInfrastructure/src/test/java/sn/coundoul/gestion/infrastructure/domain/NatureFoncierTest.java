package sn.coundoul.gestion.infrastructure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.infrastructure.web.rest.TestUtil;

class NatureFoncierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NatureFoncier.class);
        NatureFoncier natureFoncier1 = new NatureFoncier();
        natureFoncier1.setId(1L);
        NatureFoncier natureFoncier2 = new NatureFoncier();
        natureFoncier2.setId(natureFoncier1.getId());
        assertThat(natureFoncier1).isEqualTo(natureFoncier2);
        natureFoncier2.setId(2L);
        assertThat(natureFoncier1).isNotEqualTo(natureFoncier2);
        natureFoncier1.setId(null);
        assertThat(natureFoncier1).isNotEqualTo(natureFoncier2);
    }
}
