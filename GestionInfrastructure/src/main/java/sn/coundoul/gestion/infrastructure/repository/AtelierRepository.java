package sn.coundoul.gestion.infrastructure.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.infrastructure.domain.Atelier;

/**
 * Spring Data SQL repository for the Atelier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AtelierRepository extends JpaRepository<Atelier, Long> {}
