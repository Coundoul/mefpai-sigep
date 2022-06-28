package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Formateurs;

/**
 * Spring Data SQL reactive repository for the Formateurs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormateursRepository extends R2dbcRepository<Formateurs, Long>, FormateursRepositoryInternal {
    Flux<Formateurs> findAllBy(Pageable pageable);

    @Query("SELECT * FROM formateurs entity WHERE entity.nom_etablissement_id = :id")
    Flux<Formateurs> findByNomEtablissement(Long id);

    @Query("SELECT * FROM formateurs entity WHERE entity.nom_etablissement_id IS NULL")
    Flux<Formateurs> findAllWhereNomEtablissementIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Formateurs> findAll();

    @Override
    Mono<Formateurs> findById(Long id);

    @Override
    <S extends Formateurs> Mono<S> save(S entity);
}

interface FormateursRepositoryInternal {
    <S extends Formateurs> Mono<S> insert(S entity);
    <S extends Formateurs> Mono<S> save(S entity);
    Mono<Integer> update(Formateurs entity);

    Flux<Formateurs> findAll();
    Mono<Formateurs> findById(Long id);
    Flux<Formateurs> findAllBy(Pageable pageable);
    Flux<Formateurs> findAllBy(Pageable pageable, Criteria criteria);
}
