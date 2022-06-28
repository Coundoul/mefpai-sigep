package sn.coundoul.gestion.equipement.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.equipement.domain.UtilisateurFinal;

/**
 * Spring Data SQL repository for the UtilisateurFinal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UtilisateurFinalRepository extends JpaRepository<UtilisateurFinal, Long> {}
