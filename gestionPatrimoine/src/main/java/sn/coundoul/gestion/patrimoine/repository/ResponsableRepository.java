package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Responsable;

/**
 * Spring Data SQL reactive repository for the Responsable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResponsableRepository extends R2dbcRepository<Responsable, Long>, ResponsableRepositoryInternal {
    Flux<Responsable> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<Responsable> findAll();

    @Override
    Mono<Responsable> findById(Long id);

    @Override
    <S extends Responsable> Mono<S> save(S entity);
}

interface ResponsableRepositoryInternal {
    <S extends Responsable> Mono<S> insert(S entity);
    <S extends Responsable> Mono<S> save(S entity);
    Mono<Integer> update(Responsable entity);

    Flux<Responsable> findAll();
    Mono<Responsable> findById(Long id);
    Flux<Responsable> findAllBy(Pageable pageable);
    Flux<Responsable> findAllBy(Pageable pageable, Criteria criteria);
}
