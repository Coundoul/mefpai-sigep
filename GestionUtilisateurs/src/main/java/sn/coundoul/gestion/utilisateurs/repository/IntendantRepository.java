package sn.coundoul.gestion.utilisateurs.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.utilisateurs.domain.Intendant;

/**
 * Spring Data SQL repository for the Intendant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntendantRepository extends JpaRepository<Intendant, Long> {}
