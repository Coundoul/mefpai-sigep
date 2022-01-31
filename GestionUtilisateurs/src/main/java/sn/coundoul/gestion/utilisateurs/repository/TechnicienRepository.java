package sn.coundoul.gestion.utilisateurs.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.utilisateurs.domain.Technicien;

/**
 * Spring Data SQL repository for the Technicien entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TechnicienRepository extends JpaRepository<Technicien, Long> {}
