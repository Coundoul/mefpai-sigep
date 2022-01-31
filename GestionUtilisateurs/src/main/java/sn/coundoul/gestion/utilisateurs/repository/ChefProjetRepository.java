package sn.coundoul.gestion.utilisateurs.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.utilisateurs.domain.ChefProjet;

/**
 * Spring Data SQL repository for the ChefProjet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChefProjetRepository extends JpaRepository<ChefProjet, Long> {}
