package sn.coundoul.gestion.utilisateurs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.coundoul.gestion.utilisateurs.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
