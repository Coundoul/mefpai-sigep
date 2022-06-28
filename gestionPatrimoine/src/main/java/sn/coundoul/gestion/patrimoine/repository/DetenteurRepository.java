package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Detenteur;

/**
 * Spring Data SQL reactive repository for the Detenteur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetenteurRepository extends R2dbcRepository<Detenteur, Long>, DetenteurRepositoryInternal {
    Flux<Detenteur> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<Detenteur> findAll();

    @Override
    Mono<Detenteur> findById(Long id);

    @Override
    <S extends Detenteur> Mono<S> save(S entity);
}

interface DetenteurRepositoryInternal {
    <S extends Detenteur> Mono<S> insert(S entity);
    <S extends Detenteur> Mono<S> save(S entity);
    Mono<Integer> update(Detenteur entity);

    Flux<Detenteur> findAll();
    Mono<Detenteur> findById(Long id);
    Flux<Detenteur> findAllBy(Pageable pageable);
    Flux<Detenteur> findAllBy(Pageable pageable, Criteria criteria);
}
