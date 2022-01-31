package sn.coundoul.gestion.infrastructure.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.infrastructure.domain.Responsable;

/**
 * Spring Data SQL repository for the Responsable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResponsableRepository extends JpaRepository<Responsable, Long> {}
