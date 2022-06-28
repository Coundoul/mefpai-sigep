package sn.coundoul.gestion.equipement.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.equipement.domain.Affectations;

/**
 * Spring Data SQL repository for the Affectations entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AffectationsRepository extends JpaRepository<Affectations, Long> {}
