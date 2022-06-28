package sn.coundoul.gestion.infrastructure.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.infrastructure.domain.Salles;

/**
 * Spring Data SQL repository for the Salles entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SallesRepository extends JpaRepository<Salles, Long> {}
