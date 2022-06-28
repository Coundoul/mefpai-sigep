package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Intendant;

/**
 * Spring Data SQL reactive repository for the Intendant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntendantRepository extends R2dbcRepository<Intendant, Long>, IntendantRepositoryInternal {
    Flux<Intendant> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<Intendant> findAll();

    @Override
    Mono<Intendant> findById(Long id);

    @Override
    <S extends Intendant> Mono<S> save(S entity);
}

interface IntendantRepositoryInternal {
    <S extends Intendant> Mono<S> insert(S entity);
    <S extends Intendant> Mono<S> save(S entity);
    Mono<Integer> update(Intendant entity);

    Flux<Intendant> findAll();
    Mono<Intendant> findById(Long id);
    Flux<Intendant> findAllBy(Pageable pageable);
    Flux<Intendant> findAllBy(Pageable pageable, Criteria criteria);
}
