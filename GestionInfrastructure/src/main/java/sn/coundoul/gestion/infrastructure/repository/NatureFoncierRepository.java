package sn.coundoul.gestion.infrastructure.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.infrastructure.domain.NatureFoncier;

/**
 * Spring Data SQL repository for the NatureFoncier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NatureFoncierRepository extends JpaRepository<NatureFoncier, Long> {}
