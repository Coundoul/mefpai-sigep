package sn.coundoul.gestion.maintenance.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.maintenance.domain.Requete;

/**
 * Spring Data SQL repository for the Requete entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequeteRepository extends JpaRepository<Requete, Long> {}
