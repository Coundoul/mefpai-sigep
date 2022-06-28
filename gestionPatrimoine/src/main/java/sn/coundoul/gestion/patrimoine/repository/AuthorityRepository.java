package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import sn.coundoul.gestion.patrimoine.domain.Authority;

/**
 * Spring Data R2DBC repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends R2dbcRepository<Authority, String> {}
