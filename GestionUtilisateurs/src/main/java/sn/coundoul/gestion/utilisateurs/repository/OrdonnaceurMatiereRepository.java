package sn.coundoul.gestion.utilisateurs.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.utilisateurs.domain.OrdonnaceurMatiere;

/**
 * Spring Data SQL repository for the OrdonnaceurMatiere entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdonnaceurMatiereRepository extends JpaRepository<OrdonnaceurMatiere, Long> {}
