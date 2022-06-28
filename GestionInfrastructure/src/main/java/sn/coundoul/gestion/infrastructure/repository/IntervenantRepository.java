package sn.coundoul.gestion.infrastructure.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.infrastructure.domain.Intervenant;

/**
 * Spring Data SQL repository for the Intervenant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntervenantRepository extends JpaRepository<Intervenant, Long> {}
