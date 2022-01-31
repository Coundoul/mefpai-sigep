package sn.coundoul.gestion.equipement.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.equipement.domain.Attribution;

/**
 * Spring Data SQL repository for the Attribution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttributionRepository extends JpaRepository<Attribution, Long> {}
