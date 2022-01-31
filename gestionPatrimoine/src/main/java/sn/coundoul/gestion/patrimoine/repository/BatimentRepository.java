package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Batiment;

/**
 * Spring Data SQL reactive repository for the Batiment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BatimentRepository extends R2dbcRepository<Batiment, Long>, BatimentRepositoryInternal {
    Flux<Batiment> findAllBy(Pageable pageable);

    @Query("SELECT * FROM batiment entity WHERE entity.nom_etablissement_id = :id")
    Flux<Batiment> findByNomEtablissement(Long id);

    @Query("SELECT * FROM batiment entity WHERE entity.nom_etablissement_id IS NULL")
    Flux<Batiment> findAllWhereNomEtablissementIsNull();

    @Query("SELECT * FROM batiment entity WHERE entity.nom_corps_id = :id")
    Flux<Batiment> findByNomCorps(Long id);

    @Query("SELECT * FROM batiment entity WHERE entity.nom_corps_id IS NULL")
    Flux<Batiment> findAllWhereNomCorpsIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Batiment> findAll();

    @Override
    Mono<Batiment> findById(Long id);

    @Override
    <S extends Batiment> Mono<S> save(S entity);
}

interface BatimentRepositoryInternal {
    <S extends Batiment> Mono<S> insert(S entity);
    <S extends Batiment> Mono<S> save(S entity);
    Mono<Integer> update(Batiment entity);

    Flux<Batiment> findAll();
    Mono<Batiment> findById(Long id);
    Flux<Batiment> findAllBy(Pageable pageable);
    Flux<Batiment> findAllBy(Pageable pageable, Criteria criteria);
}
