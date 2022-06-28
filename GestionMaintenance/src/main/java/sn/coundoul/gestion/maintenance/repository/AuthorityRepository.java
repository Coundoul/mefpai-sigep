package sn.coundoul.gestion.maintenance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.coundoul.gestion.maintenance.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
