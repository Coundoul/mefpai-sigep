package sn.coundoul.gestion.maintenance.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.maintenance.domain.FicheTechniqueMaintenance;

/**
 * Spring Data SQL repository for the FicheTechniqueMaintenance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FicheTechniqueMaintenanceRepository extends JpaRepository<FicheTechniqueMaintenance, Long> {}
