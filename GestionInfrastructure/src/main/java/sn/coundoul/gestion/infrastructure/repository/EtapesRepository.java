package sn.coundoul.gestion.infrastructure.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.infrastructure.domain.Etapes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data SQL repository for the Etapes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EtapesRepository extends JpaRepository<Etapes, Long> {
    @Query("select etapes from Etapes etapes where nom_projet_id=:id")
    Page<Etapes> findAllEtapesProjet(@Param("id") Long id, Pageable pageable);
}
