package sn.coundoul.gestion.infrastructure.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.infrastructure.domain.Projets;

/**
 * Spring Data SQL repository for the Projets entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjetsRepository extends JpaRepository<Projets, Long> {}
