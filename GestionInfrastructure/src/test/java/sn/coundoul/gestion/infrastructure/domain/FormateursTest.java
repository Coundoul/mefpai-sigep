package sn.coundoul.gestion.infrastructure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.infrastructure.web.rest.TestUtil;

class FormateursTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Formateurs.class);
        Formateurs formateurs1 = new Formateurs();
        formateurs1.setId(1L);
        Formateurs formateurs2 = new Formateurs();
        formateurs2.setId(formateurs1.getId());
        assertThat(formateurs1).isEqualTo(formateurs2);
        formateurs2.setId(2L);
        assertThat(formateurs1).isNotEqualTo(formateurs2);
        formateurs1.setId(null);
        assertThat(formateurs1).isNotEqualTo(formateurs2);
    }
}
