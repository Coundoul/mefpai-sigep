package sn.coundoul.gestion.infrastructure.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.infrastructure.domain.AttributionInfrastructure;

/**
 * Spring Data SQL repository for the AttributionInfrastructure entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttributionInfrastructureRepository extends JpaRepository<AttributionInfrastructure, Long> {}
