package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.CorpsEtat;

/**
 * Spring Data SQL reactive repository for the CorpsEtat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorpsEtatRepository extends R2dbcRepository<CorpsEtat, Long>, CorpsEtatRepositoryInternal {
    Flux<CorpsEtat> findAllBy(Pageable pageable);

    @Query("SELECT * FROM corps_etat entity WHERE entity.nom_responsable_id = :id")
    Flux<CorpsEtat> findByNomResponsable(Long id);

    @Query("SELECT * FROM corps_etat entity WHERE entity.nom_responsable_id IS NULL")
    Flux<CorpsEtat> findAllWhereNomResponsableIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<CorpsEtat> findAll();

    @Override
    Mono<CorpsEtat> findById(Long id);

    @Override
    <S extends CorpsEtat> Mono<S> save(S entity);
}

interface CorpsEtatRepositoryInternal {
    <S extends CorpsEtat> Mono<S> insert(S entity);
    <S extends CorpsEtat> Mono<S> save(S entity);
    Mono<Integer> update(CorpsEtat entity);

    Flux<CorpsEtat> findAll();
    Mono<CorpsEtat> findById(Long id);
    Flux<CorpsEtat> findAllBy(Pageable pageable);
    Flux<CorpsEtat> findAllBy(Pageable pageable, Criteria criteria);
}
