package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Magazin;

/**
 * Spring Data SQL reactive repository for the Magazin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MagazinRepository extends R2dbcRepository<Magazin, Long>, MagazinRepositoryInternal {
    Flux<Magazin> findAllBy(Pageable pageable);

    @Query("SELECT * FROM magazin entity WHERE entity.nom_quartier_id = :id")
    Flux<Magazin> findByNomQuartier(Long id);

    @Query("SELECT * FROM magazin entity WHERE entity.nom_quartier_id IS NULL")
    Flux<Magazin> findAllWhereNomQuartierIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Magazin> findAll();

    @Override
    Mono<Magazin> findById(Long id);

    @Override
    <S extends Magazin> Mono<S> save(S entity);
}

interface MagazinRepositoryInternal {
    <S extends Magazin> Mono<S> insert(S entity);
    <S extends Magazin> Mono<S> save(S entity);
    Mono<Integer> update(Magazin entity);

    Flux<Magazin> findAll();
    Mono<Magazin> findById(Long id);
    Flux<Magazin> findAllBy(Pageable pageable);
    Flux<Magazin> findAllBy(Pageable pageable, Criteria criteria);
}
