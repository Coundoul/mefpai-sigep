package sn.coundoul.gestion.infrastructure.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.infrastructure.domain.TypeBatiment;

/**
 * Spring Data SQL repository for the TypeBatiment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeBatimentRepository extends JpaRepository<TypeBatiment, Long> {}
