package sn.coundoul.gestion.infrastructure.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.infrastructure.domain.FiliereStabilise;

/**
 * Spring Data SQL repository for the FiliereStabilise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FiliereStabiliseRepository extends JpaRepository<FiliereStabilise, Long> {}
