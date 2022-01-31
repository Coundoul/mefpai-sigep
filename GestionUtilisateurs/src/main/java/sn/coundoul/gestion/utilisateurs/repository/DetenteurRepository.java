package sn.coundoul.gestion.utilisateurs.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.utilisateurs.domain.Detenteur;

/**
 * Spring Data SQL repository for the Detenteur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetenteurRepository extends JpaRepository<Detenteur, Long> {}
