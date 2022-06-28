package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.ChefProjet;

/**
 * Spring Data SQL reactive repository for the ChefProjet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChefProjetRepository extends R2dbcRepository<ChefProjet, Long>, ChefProjetRepositoryInternal {
    Flux<ChefProjet> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<ChefProjet> findAll();

    @Override
    Mono<ChefProjet> findById(Long id);

    @Override
    <S extends ChefProjet> Mono<S> save(S entity);
}

interface ChefProjetRepositoryInternal {
    <S extends ChefProjet> Mono<S> insert(S entity);
    <S extends ChefProjet> Mono<S> save(S entity);
    Mono<Integer> update(ChefProjet entity);

    Flux<ChefProjet> findAll();
    Mono<ChefProjet> findById(Long id);
    Flux<ChefProjet> findAllBy(Pageable pageable);
    Flux<ChefProjet> findAllBy(Pageable pageable, Criteria criteria);
}
