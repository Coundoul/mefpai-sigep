package sn.coundoul.gestion.utilisateurs.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.utilisateurs.domain.ChefMaintenance;

/**
 * Spring Data SQL repository for the ChefMaintenance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChefMaintenanceRepository extends JpaRepository<ChefMaintenance, Long> {}
