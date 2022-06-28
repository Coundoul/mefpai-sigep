package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Fournisseur;

/**
 * Spring Data SQL reactive repository for the Fournisseur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FournisseurRepository extends R2dbcRepository<Fournisseur, Long>, FournisseurRepositoryInternal {
    Flux<Fournisseur> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<Fournisseur> findAll();

    @Override
    Mono<Fournisseur> findById(Long id);

    @Override
    <S extends Fournisseur> Mono<S> save(S entity);
}

interface FournisseurRepositoryInternal {
    <S extends Fournisseur> Mono<S> insert(S entity);
    <S extends Fournisseur> Mono<S> save(S entity);
    Mono<Integer> update(Fournisseur entity);

    Flux<Fournisseur> findAll();
    Mono<Fournisseur> findById(Long id);
    Flux<Fournisseur> findAllBy(Pageable pageable);
    Flux<Fournisseur> findAllBy(Pageable pageable, Criteria criteria);
}
