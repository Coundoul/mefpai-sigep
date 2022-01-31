package sn.coundoul.gestion.infrastructure.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.infrastructure.domain.ContratProjet;

/**
 * Spring Data SQL repository for the ContratProjet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContratProjetRepository extends JpaRepository<ContratProjet, Long> {}
