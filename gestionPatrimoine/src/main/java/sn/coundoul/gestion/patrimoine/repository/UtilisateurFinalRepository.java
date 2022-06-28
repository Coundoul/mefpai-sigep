package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.UtilisateurFinal;

/**
 * Spring Data SQL reactive repository for the UtilisateurFinal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UtilisateurFinalRepository extends R2dbcRepository<UtilisateurFinal, Long>, UtilisateurFinalRepositoryInternal {
    Flux<UtilisateurFinal> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<UtilisateurFinal> findAll();

    @Override
    Mono<UtilisateurFinal> findById(Long id);

    @Override
    <S extends UtilisateurFinal> Mono<S> save(S entity);
}

interface UtilisateurFinalRepositoryInternal {
    <S extends UtilisateurFinal> Mono<S> insert(S entity);
    <S extends UtilisateurFinal> Mono<S> save(S entity);
    Mono<Integer> update(UtilisateurFinal entity);

    Flux<UtilisateurFinal> findAll();
    Mono<UtilisateurFinal> findById(Long id);
    Flux<UtilisateurFinal> findAllBy(Pageable pageable);
    Flux<UtilisateurFinal> findAllBy(Pageable pageable, Criteria criteria);
}
