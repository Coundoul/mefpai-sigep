package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Directeur;

/**
 * Spring Data SQL reactive repository for the Directeur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DirecteurRepository extends R2dbcRepository<Directeur, Long>, DirecteurRepositoryInternal {
    Flux<Directeur> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<Directeur> findAll();

    @Override
    Mono<Directeur> findById(Long id);

    @Override
    <S extends Directeur> Mono<S> save(S entity);
}

interface DirecteurRepositoryInternal {
    <S extends Directeur> Mono<S> insert(S entity);
    <S extends Directeur> Mono<S> save(S entity);
    Mono<Integer> update(Directeur entity);

    Flux<Directeur> findAll();
    Mono<Directeur> findById(Long id);
    Flux<Directeur> findAllBy(Pageable pageable);
    Flux<Directeur> findAllBy(Pageable pageable, Criteria criteria);
}
