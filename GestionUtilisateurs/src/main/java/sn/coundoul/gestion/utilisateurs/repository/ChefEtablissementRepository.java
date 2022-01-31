package sn.coundoul.gestion.utilisateurs.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.utilisateurs.domain.ChefEtablissement;

/**
 * Spring Data SQL repository for the ChefEtablissement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChefEtablissementRepository extends JpaRepository<ChefEtablissement, Long> {}
