package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Bon;

/**
 * Spring Data SQL reactive repository for the Bon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BonRepository extends R2dbcRepository<Bon, Long>, BonRepositoryInternal {
    Flux<Bon> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<Bon> findAll();

    @Override
    Mono<Bon> findById(Long id);

    @Override
    <S extends Bon> Mono<S> save(S entity);
}

interface BonRepositoryInternal {
    <S extends Bon> Mono<S> insert(S entity);
    <S extends Bon> Mono<S> save(S entity);
    Mono<Integer> update(Bon entity);

    Flux<Bon> findAll();
    Mono<Bon> findById(Long id);
    Flux<Bon> findAllBy(Pageable pageable);
    Flux<Bon> findAllBy(Pageable pageable, Criteria criteria);
}
