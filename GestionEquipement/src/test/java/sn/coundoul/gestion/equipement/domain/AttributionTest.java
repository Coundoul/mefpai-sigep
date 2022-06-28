package sn.coundoul.gestion.equipement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.equipement.web.rest.TestUtil;

class AttributionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attribution.class);
        Attribution attribution1 = new Attribution();
        attribution1.setId(1L);
        Attribution attribution2 = new Attribution();
        attribution2.setId(attribution1.getId());
        assertThat(attribution1).isEqualTo(attribution2);
        attribution2.setId(2L);
        assertThat(attribution1).isNotEqualTo(attribution2);
        attribution1.setId(null);
        assertThat(attribution1).isNotEqualTo(attribution2);
    }
}
