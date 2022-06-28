package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.AttributionInfrastructure;

/**
 * Spring Data SQL reactive repository for the AttributionInfrastructure entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttributionInfrastructureRepository
    extends R2dbcRepository<AttributionInfrastructure, Long>, AttributionInfrastructureRepositoryInternal {
    Flux<AttributionInfrastructure> findAllBy(Pageable pageable);

    @Query("SELECT * FROM attribution_infrastructure entity WHERE entity.nom_etablissement_id = :id")
    Flux<AttributionInfrastructure> findByNomEtablissement(Long id);

    @Query("SELECT * FROM attribution_infrastructure entity WHERE entity.nom_etablissement_id IS NULL")
    Flux<AttributionInfrastructure> findAllWhereNomEtablissementIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<AttributionInfrastructure> findAll();

    @Override
    Mono<AttributionInfrastructure> findById(Long id);

    @Override
    <S extends AttributionInfrastructure> Mono<S> save(S entity);
}

interface AttributionInfrastructureRepositoryInternal {
    <S extends AttributionInfrastructure> Mono<S> insert(S entity);
    <S extends AttributionInfrastructure> Mono<S> save(S entity);
    Mono<Integer> update(AttributionInfrastructure entity);

    Flux<AttributionInfrastructure> findAll();
    Mono<AttributionInfrastructure> findById(Long id);
    Flux<AttributionInfrastructure> findAllBy(Pageable pageable);
    Flux<AttributionInfrastructure> findAllBy(Pageable pageable, Criteria criteria);
}
