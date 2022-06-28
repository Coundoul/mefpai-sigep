package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Commune;

/**
 * Spring Data SQL reactive repository for the Commune entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommuneRepository extends R2dbcRepository<Commune, Long>, CommuneRepositoryInternal {
    Flux<Commune> findAllBy(Pageable pageable);

    @Query("SELECT * FROM commune entity WHERE entity.nom_departement_id = :id")
    Flux<Commune> findByNomDepartement(Long id);

    @Query("SELECT * FROM commune entity WHERE entity.nom_departement_id IS NULL")
    Flux<Commune> findAllWhereNomDepartementIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Commune> findAll();

    @Override
    Mono<Commune> findById(Long id);

    @Override
    <S extends Commune> Mono<S> save(S entity);
}

interface CommuneRepositoryInternal {
    <S extends Commune> Mono<S> insert(S entity);
    <S extends Commune> Mono<S> save(S entity);
    Mono<Integer> update(Commune entity);

    Flux<Commune> findAll();
    Mono<Commune> findById(Long id);
    Flux<Commune> findAllBy(Pageable pageable);
    Flux<Commune> findAllBy(Pageable pageable, Criteria criteria);
}
