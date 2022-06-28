package sn.coundoul.gestion.equipement.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.equipement.domain.Bon;

/**
 * Spring Data SQL repository for the Bon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BonRepository extends JpaRepository<Bon, Long> {}
