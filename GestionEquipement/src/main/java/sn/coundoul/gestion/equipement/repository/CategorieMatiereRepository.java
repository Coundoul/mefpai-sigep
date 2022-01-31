package sn.coundoul.gestion.equipement.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.equipement.domain.CategorieMatiere;

/**
 * Spring Data SQL repository for the CategorieMatiere entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategorieMatiereRepository extends JpaRepository<CategorieMatiere, Long> {}
