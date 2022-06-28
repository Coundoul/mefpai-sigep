package sn.coundoul.gestion.maintenance.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.maintenance.domain.Bureau;

/**
 * Spring Data SQL repository for the Bureau entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BureauRepository extends JpaRepository<Bureau, Long> {}
