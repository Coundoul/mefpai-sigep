package sn.coundoul.gestion.equipement.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.equipement.domain.Magazin;

/**
 * Spring Data SQL repository for the Magazin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MagazinRepository extends JpaRepository<Magazin, Long> {}
