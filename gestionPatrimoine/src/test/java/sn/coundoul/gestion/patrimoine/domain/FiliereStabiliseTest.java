package sn.coundoul.gestion.patrimoine.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.patrimoine.web.rest.TestUtil;

class FiliereStabiliseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FiliereStabilise.class);
        FiliereStabilise filiereStabilise1 = new FiliereStabilise();
        filiereStabilise1.setId(1L);
        FiliereStabilise filiereStabilise2 = new FiliereStabilise();
        filiereStabilise2.setId(filiereStabilise1.getId());
        assertThat(filiereStabilise1).isEqualTo(filiereStabilise2);
        filiereStabilise2.setId(2L);
        assertThat(filiereStabilise1).isNotEqualTo(filiereStabilise2);
        filiereStabilise1.setId(null);
        assertThat(filiereStabilise1).isNotEqualTo(filiereStabilise2);
    }
}
