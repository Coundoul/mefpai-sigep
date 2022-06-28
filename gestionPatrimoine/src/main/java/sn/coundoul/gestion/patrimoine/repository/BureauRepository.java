package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Bureau;

/**
 * Spring Data SQL reactive repository for the Bureau entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BureauRepository extends R2dbcRepository<Bureau, Long>, BureauRepositoryInternal {
    Flux<Bureau> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<Bureau> findAll();

    @Override
    Mono<Bureau> findById(Long id);

    @Override
    <S extends Bureau> Mono<S> save(S entity);
}

interface BureauRepositoryInternal {
    <S extends Bureau> Mono<S> insert(S entity);
    <S extends Bureau> Mono<S> save(S entity);
    Mono<Integer> update(Bureau entity);

    Flux<Bureau> findAll();
    Mono<Bureau> findById(Long id);
    Flux<Bureau> findAllBy(Pageable pageable);
    Flux<Bureau> findAllBy(Pageable pageable, Criteria criteria);
}
