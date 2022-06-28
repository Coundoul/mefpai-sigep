package sn.coundoul.gestion.infrastructure.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.infrastructure.domain.ProjetAttribution;

/**
 * Spring Data SQL repository for the ProjetAttribution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjetAttributionRepository extends JpaRepository<ProjetAttribution, Long> {}
