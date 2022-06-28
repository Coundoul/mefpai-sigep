package sn.coundoul.gestion.infrastructure.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.infrastructure.domain.Etapes;

/**
 * Spring Data SQL repository for the Etapes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EtapesRepository extends JpaRepository<Etapes, Long> {}
