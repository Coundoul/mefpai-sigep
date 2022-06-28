package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Etapes;

/**
 * Spring Data SQL reactive repository for the Etapes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EtapesRepository extends R2dbcRepository<Etapes, Long>, EtapesRepositoryInternal {
    Flux<Etapes> findAllBy(Pageable pageable);

    @Query("SELECT * FROM etapes entity WHERE entity.nom_projet_id = :id")
    Flux<Etapes> findByNomProjet(Long id);

    @Query("SELECT * FROM etapes entity WHERE entity.nom_projet_id IS NULL")
    Flux<Etapes> findAllWhereNomProjetIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Etapes> findAll();

    @Override
    Mono<Etapes> findById(Long id);

    @Override
    <S extends Etapes> Mono<S> save(S entity);
}

interface EtapesRepositoryInternal {
    <S extends Etapes> Mono<S> insert(S entity);
    <S extends Etapes> Mono<S> save(S entity);
    Mono<Integer> update(Etapes entity);

    Flux<Etapes> findAll();
    Mono<Etapes> findById(Long id);
    Flux<Etapes> findAllBy(Pageable pageable);
    Flux<Etapes> findAllBy(Pageable pageable, Criteria criteria);
}
