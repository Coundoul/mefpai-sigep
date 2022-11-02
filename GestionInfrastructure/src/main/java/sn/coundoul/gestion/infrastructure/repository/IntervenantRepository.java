package sn.coundoul.gestion.infrastructure.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.coundoul.gestion.infrastructure.domain.Intervenant;

/**
 * Spring Data SQL repository for the Intervenant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntervenantRepository extends JpaRepository<Intervenant, Long> {
    @Query("select intervenant from Intervenant intervenant where nom_projet_id =:id")
    Page<Intervenant> findAllIntervenant(@Param("id") Long id, Pageable pageable);
}
