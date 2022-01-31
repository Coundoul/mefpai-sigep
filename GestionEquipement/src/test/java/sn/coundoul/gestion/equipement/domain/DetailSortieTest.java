package sn.coundoul.gestion.equipement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.equipement.web.rest.TestUtil;

class DetailSortieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetailSortie.class);
        DetailSortie detailSortie1 = new DetailSortie();
        detailSortie1.setId(1L);
        DetailSortie detailSortie2 = new DetailSortie();
        detailSortie2.setId(detailSortie1.getId());
        assertThat(detailSortie1).isEqualTo(detailSortie2);
        detailSortie2.setId(2L);
        assertThat(detailSortie1).isNotEqualTo(detailSortie2);
        detailSortie1.setId(null);
        assertThat(detailSortie1).isNotEqualTo(detailSortie2);
    }
}
