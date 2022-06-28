package sn.coundoul.gestion.utilisateurs.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.coundoul.gestion.utilisateurs.domain.MangasinierFichiste;

/**
 * Spring Data SQL repository for the MangasinierFichiste entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MangasinierFichisteRepository extends JpaRepository<MangasinierFichiste, Long> {}
