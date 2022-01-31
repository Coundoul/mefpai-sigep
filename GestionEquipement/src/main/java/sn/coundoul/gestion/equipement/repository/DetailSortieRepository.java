package sn.coundoul.gestion.equipement.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.equipement.domain.DetailSortie;

/**
 * Spring Data SQL repository for the DetailSortie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetailSortieRepository extends JpaRepository<DetailSortie, Long> {}
