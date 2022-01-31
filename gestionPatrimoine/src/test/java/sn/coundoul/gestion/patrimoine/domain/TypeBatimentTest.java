package sn.coundoul.gestion.patrimoine.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.patrimoine.web.rest.TestUtil;

class TypeBatimentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeBatiment.class);
        TypeBatiment typeBatiment1 = new TypeBatiment();
        typeBatiment1.setId(1L);
        TypeBatiment typeBatiment2 = new TypeBatiment();
        typeBatiment2.setId(typeBatiment1.getId());
        assertThat(typeBatiment1).isEqualTo(typeBatiment2);
        typeBatiment2.setId(2L);
        assertThat(typeBatiment1).isNotEqualTo(typeBatiment2);
        typeBatiment1.setId(null);
        assertThat(typeBatiment1).isNotEqualTo(typeBatiment2);
    }
}
