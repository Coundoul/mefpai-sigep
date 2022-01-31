package sn.coundoul.gestion.utilisateurs.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.utilisateurs.domain.Directeur;

/**
 * Spring Data SQL repository for the Directeur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DirecteurRepository extends JpaRepository<Directeur, Long> {}
