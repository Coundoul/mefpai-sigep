package sn.coundoul.gestion.infrastructure.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.infrastructure.domain.Formateurs;

/**
 * Spring Data SQL repository for the Formateurs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormateursRepository extends JpaRepository<Formateurs, Long> {}
