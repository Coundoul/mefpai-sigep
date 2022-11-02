package sn.coundoul.gestion.infrastructure.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import sn.coundoul.gestion.infrastructure.domain.Projets;

/**
 * Spring Data SQL repository for the Projets entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjetsRepository extends JpaRepository<Projets, Long> {
    @Query("select projets from Projets projets where nom_etablissement_id =:id")
    Page<Projets> findAllProjet(@Param("id") Long id, Pageable pageable);
}
