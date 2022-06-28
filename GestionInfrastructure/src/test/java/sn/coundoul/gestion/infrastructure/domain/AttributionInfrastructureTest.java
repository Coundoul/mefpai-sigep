package sn.coundoul.gestion.infrastructure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.coundoul.gestion.infrastructure.web.rest.TestUtil;

class AttributionInfrastructureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttributionInfrastructure.class);
        AttributionInfrastructure attributionInfrastructure1 = new AttributionInfrastructure();
        attributionInfrastructure1.setId(1L);
        AttributionInfrastructure attributionInfrastructure2 = new AttributionInfrastructure();
        attributionInfrastructure2.setId(attributionInfrastructure1.getId());
        assertThat(attributionInfrastructure1).isEqualTo(attributionInfrastructure2);
        attributionInfrastructure2.setId(2L);
        assertThat(attributionInfrastructure1).isNotEqualTo(attributionInfrastructure2);
        attributionInfrastructure1.setId(null);
        assertThat(attributionInfrastructure1).isNotEqualTo(attributionInfrastructure2);
    }
}
