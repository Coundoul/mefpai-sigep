package sn.coundoul.gestion.infrastructure.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.infrastructure.domain.CorpsEtat;

/**
 * Spring Data SQL repository for the CorpsEtat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorpsEtatRepository extends JpaRepository<CorpsEtat, Long> {}
