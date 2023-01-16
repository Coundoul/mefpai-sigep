package sn.coundoul.gestion.infrastructure.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.infrastructure.domain.Batiment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data SQL repository for the Batiment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BatimentRepository extends JpaRepository<Batiment, Long> {
    @Query("select batiment from Batiment batiment where nom_etablissement_id =:id")
    Page<Batiment> findAllDesignation(@Param("id") Long id, Pageable pageable);

    @Query("select batiment from Batiment batiment where etat_gros_oeuvre='Vétuste' or etat_gros_oeuvre='Acceptable' or etat_second_oeuvre='Vétuste' or etat_second_oeuvre='Acceptable'")
    Page<Batiment> findAllBatimentEtat(Pageable pageable);
}
