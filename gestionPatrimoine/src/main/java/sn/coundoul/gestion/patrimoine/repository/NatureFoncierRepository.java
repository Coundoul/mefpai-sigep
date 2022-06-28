package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.NatureFoncier;

/**
 * Spring Data SQL reactive repository for the NatureFoncier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NatureFoncierRepository extends R2dbcRepository<NatureFoncier, Long>, NatureFoncierRepositoryInternal {
    Flux<NatureFoncier> findAllBy(Pageable pageable);

    @Query("SELECT * FROM nature_foncier entity WHERE entity.nom_corps_id = :id")
    Flux<NatureFoncier> findByNomCorps(Long id);

    @Query("SELECT * FROM nature_foncier entity WHERE entity.nom_corps_id IS NULL")
    Flux<NatureFoncier> findAllWhereNomCorpsIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<NatureFoncier> findAll();

    @Override
    Mono<NatureFoncier> findById(Long id);

    @Override
    <S extends NatureFoncier> Mono<S> save(S entity);
}

interface NatureFoncierRepositoryInternal {
    <S extends NatureFoncier> Mono<S> insert(S entity);
    <S extends NatureFoncier> Mono<S> save(S entity);
    Mono<Integer> update(NatureFoncier entity);

    Flux<NatureFoncier> findAll();
    Mono<NatureFoncier> findById(Long id);
    Flux<NatureFoncier> findAllBy(Pageable pageable);
    Flux<NatureFoncier> findAllBy(Pageable pageable, Criteria criteria);
}
