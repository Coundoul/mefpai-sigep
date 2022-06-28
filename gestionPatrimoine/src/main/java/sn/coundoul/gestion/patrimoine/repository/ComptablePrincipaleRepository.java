package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.ComptablePrincipale;

/**
 * Spring Data SQL reactive repository for the ComptablePrincipale entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComptablePrincipaleRepository extends R2dbcRepository<ComptablePrincipale, Long>, ComptablePrincipaleRepositoryInternal {
    Flux<ComptablePrincipale> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<ComptablePrincipale> findAll();

    @Override
    Mono<ComptablePrincipale> findById(Long id);

    @Override
    <S extends ComptablePrincipale> Mono<S> save(S entity);
}

interface ComptablePrincipaleRepositoryInternal {
    <S extends ComptablePrincipale> Mono<S> insert(S entity);
    <S extends ComptablePrincipale> Mono<S> save(S entity);
    Mono<Integer> update(ComptablePrincipale entity);

    Flux<ComptablePrincipale> findAll();
    Mono<ComptablePrincipale> findById(Long id);
    Flux<ComptablePrincipale> findAllBy(Pageable pageable);
    Flux<ComptablePrincipale> findAllBy(Pageable pageable, Criteria criteria);
}
