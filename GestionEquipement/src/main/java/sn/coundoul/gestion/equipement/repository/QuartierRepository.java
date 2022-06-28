package sn.coundoul.gestion.equipement.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.equipement.domain.Quartier;

/**
 * Spring Data SQL repository for the Quartier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuartierRepository extends JpaRepository<Quartier, Long> {}
