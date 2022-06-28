package sn.coundoul.gestion.equipement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.coundoul.gestion.equipement.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
