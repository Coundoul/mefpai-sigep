package sn.coundoul.gestion.utilisateurs.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.utilisateurs.domain.ComptablePrincipale;

/**
 * Spring Data SQL repository for the ComptablePrincipale entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComptablePrincipaleRepository extends JpaRepository<ComptablePrincipale, Long> {}
