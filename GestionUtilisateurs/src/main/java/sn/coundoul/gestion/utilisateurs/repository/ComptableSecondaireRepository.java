package sn.coundoul.gestion.utilisateurs.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.utilisateurs.domain.ComptableSecondaire;

/**
 * Spring Data SQL repository for the ComptableSecondaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComptableSecondaireRepository extends JpaRepository<ComptableSecondaire, Long> {}
